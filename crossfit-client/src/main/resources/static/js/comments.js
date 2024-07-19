const postCommentElement = document.getElementById('postComment');
postCommentElement.addEventListener('click', createComment);

function createComment() {
    console.log('createComment function called');
    const commentTextElement = document.querySelector('textarea[name="description"]');
    const textContent = commentTextElement.value.trim();
    const trainingType = document.querySelector('meta[name="trainingType"]').getAttribute('content');

    if (textContent.length === 0) {
        alert('Comment cannot be empty');
        return;
    }

    const requestBody = { description: textContent };

    fetch(`http://localhost:8080/workouts/details/comment/${trainingType}`, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        },
        body: JSON.stringify(requestBody)
    })
        .then(response => response.json())
        .then(data => {
            const comment = createCommentElement(data, trainingType);
            document.querySelector('.be-comment-block').appendChild(comment);
            commentTextElement.value = '';

            // Re-attach event listeners to new buttons
            attachDeleteListeners();
            attachLikeDislikeListeners();
        })
        .catch(error => console.error('Error:', error));
}

function createCommentElement(data, trainingType) {
    const comment = document.createElement('div');
    comment.classList.add('d-flex', 'flex-start', 'mb-4');

    const avatar = document.createElement('img');
    avatar.classList.add('rounded-circle', 'shadow-1-strong', 'me-3');
    avatar.src = 'https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(32).webp';
    avatar.alt = 'avatar';
    avatar.width = 65;
    avatar.height = 65;

    let commentsTitle = document.querySelector(".comments-title");
    let commentCount = parseInt(commentsTitle.textContent.match(/\d+/)[0]) +1;
    commentsTitle.textContent = `Comments: ${commentCount}`;


    const card = document.createElement('div');
    card.classList.add('card', 'w-100');

    const cardBody = document.createElement('div');
    cardBody.classList.add('card-body', 'p-4');

    const nameElement = document.createElement('h5');
    nameElement.textContent = `${data.authorFirstName} ${data.authorLastName}`;

    const dateElement = document.createElement('p');
    dateElement.classList.add('small');
    dateElement.textContent = data.date;

    const contentElement = document.createElement('p');
    contentElement.textContent = data.text;

    const likeDislikeButtons = document.createElement('div');
    likeDislikeButtons.classList.add('like-dislike-buttons');

    const likeButton = createLikeDislikeButton('like-button', data.id, trainingType, data.likes, 'thumbs-up');
    const dislikeButton = createLikeDislikeButton('dislike-button', data.id, trainingType, data.dislikes, 'thumbs-down');

    // Add delete button
    const deleteButton = document.createElement('button');
    deleteButton.classList.add('delete-button');
    deleteButton.dataset.commentId = data.id;
    deleteButton.dataset.trainingType = trainingType;
    deleteButton.innerHTML = '<i class="fas fa-trash-alt me-1"></i>';

    likeDislikeButtons.appendChild(likeButton);
    likeDislikeButtons.appendChild(dislikeButton);
    likeDislikeButtons.appendChild(deleteButton);

    cardBody.appendChild(nameElement);
    cardBody.appendChild(dateElement);
    cardBody.appendChild(contentElement);
    cardBody.appendChild(likeDislikeButtons);

    card.appendChild(cardBody);
    comment.appendChild(avatar);
    comment.appendChild(card);

    return comment;
}

function createLikeDislikeButton(type, commentId, trainingType, count, icon) {
    const button = document.createElement('button');
    button.classList.add(type);
    button.dataset.commentId = commentId;
    button.dataset.trainingType = trainingType;

    const iconElement = document.createElement('i');
    iconElement.classList.add('fas', `fa-${icon}`, 'me-1');

    const countElement = document.createElement('span');
    countElement.classList.add(type === 'like-button' ? 'likes' : 'dislikes');
    countElement.textContent = count;

    button.appendChild(iconElement);
    button.appendChild(countElement);

    return button;
}

function attachDeleteListeners() {
    document.querySelectorAll('.delete-button').forEach(button => {
        button.addEventListener('click', function () {
            const commentId = this.dataset.commentId;
            const trainingType = this.dataset.trainingType;

            fetch(`http://localhost:8080/workouts/details/${trainingType}/comment/${commentId}`, {
                method: 'DELETE',
                headers: {
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
                }
            })
                .then(response => {
                    if (response.status === 204) {
                        // Update the comment count
                        let commentsTitle = document.querySelector(".comments-title");
                        let commentCount = parseInt(commentsTitle.textContent.match(/\d+/)[0]) - 1;
                        commentsTitle.textContent = `Comments: ${commentCount}`;

                        // Remove the comment element from the DOM
                        this.closest('.d-flex').remove();
                    } else {
                        console.error('Error deleting comment:', response);
                    }
                })
                .catch(error => console.error('Error:', error));
        });
    });
}

function attachLikeDislikeListeners() {
    document.querySelectorAll('.like-button').forEach(button => {
        button.addEventListener('click', function () {
            handleLikeDislike(this, 'like');
        });
    });

    document.querySelectorAll('.dislike-button').forEach(button => {
        button.addEventListener('click', function () {
            handleLikeDislike(this, 'dislike');
        });
    });
}

function handleLikeDislike(button, type) {
    const commentId = button.dataset.commentId;
    const trainingType = button.dataset.trainingType;
    const url = `http://localhost:8080/workouts/details/${trainingType}/comment/${type}/${commentId}`;

    fetch(url, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
        }
    })
        .then(response => response.json())
        .then(data =>
            updateCommentUI(commentId, data))
        .catch(error => console.error('Error:', error));
}

function updateCommentUI(commentId, data) {
    console.log('Updating UI for comment ID:', commentId);
    console.log('Data received:', data);

    const commentElement = document.querySelector(`[data-comment-id="${commentId}"]`).closest('.d-flex');
    if (commentElement) {
        const likeCountElement = commentElement.querySelector('.likes');
        const dislikeCountElement = commentElement.querySelector('.dislikes');
        if (likeCountElement) likeCountElement.textContent = data.likes;
        if (dislikeCountElement) dislikeCountElement.textContent = data.dislikes;
    } else {
        console.log('Comment element not found for ID:', commentId);
    }
}

// Initial call to set up event listeners for existing comments
attachDeleteListeners();
attachLikeDislikeListeners();
