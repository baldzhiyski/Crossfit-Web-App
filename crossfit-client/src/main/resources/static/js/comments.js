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
            const comment = document.createElement('div');
            comment.classList.add('d-flex', 'flex-start', 'mb-4');
            const avatar = document.createElement('img');
            avatar.classList.add('rounded-circle', 'shadow-1-strong', 'me-3');
            avatar.src = 'https://mdbcdn.b-cdn.net/img/Photos/Avatars/img%20(32).webp';
            avatar.alt = 'avatar';
            avatar.width = 65;
            avatar.height = 65;
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
            const likeButton = document.createElement('button');
            likeButton.classList.add('like-button');
            likeButton.dataset.commentId = data.id;
            likeButton.dataset.trainingType = trainingType;
            const likeIcon = document.createElement('i');
            likeIcon.classList.add('fas', 'fa-thumbs-up', 'me-1');
            const likeCount = document.createElement('span');
            likeCount.classList.add('likes');
            likeCount.textContent = data.likes;
            likeButton.appendChild(likeIcon);
            likeButton.appendChild(likeCount);
            const dislikeButton = document.createElement('button');
            dislikeButton.classList.add('dislike-button');
            dislikeButton.dataset.commentId = data.id;
            dislikeButton.dataset.trainingType = trainingType;
            const dislikeIcon = document.createElement('i');
            dislikeIcon.classList.add('fas', 'fa-thumbs-down', 'me-1');
            const dislikeCount = document.createElement('span');
            dislikeCount.classList.add('dislikes');
            dislikeCount.textContent = data.dislikes;
            dislikeButton.appendChild(dislikeIcon);
            dislikeButton.appendChild(dislikeCount);
            likeDislikeButtons.appendChild(likeButton);
            likeDislikeButtons.appendChild(dislikeButton);
            cardBody.appendChild(nameElement);
            cardBody.appendChild(dateElement);
            cardBody.appendChild(contentElement);
            cardBody.appendChild(likeDislikeButtons);
            card.appendChild(cardBody);
            comment.appendChild(avatar);
            comment.appendChild(card);
            document.querySelector('.be-comment-block').appendChild(comment);
            commentTextElement.value = '';

            // Re-attach event listeners to new buttons
            attachLikeDislikeListeners();
        })
        .catch(error => console.error('Error:', error));
}

function attachLikeDislikeListeners() {
    document.querySelectorAll('.like-button').forEach(button => {
        button.addEventListener('click', function () {
            const commentId = this.dataset.commentId;
            const trainingType = this.dataset.trainingType;
            fetch(`http://localhost:8080/workouts/details/${trainingType}/comment/like/${commentId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
                }
            })
                .then(response => response.json())
                .then(data => updateCommentUI(commentId, data))
                .catch(error => console.error('Error:', error));
        });
    });

    document.querySelectorAll('.dislike-button').forEach(button => {
        button.addEventListener('click', function () {
            const commentId = this.dataset.commentId;
            const trainingType = this.dataset.trainingType;
            fetch(`http://localhost:8080/workouts/details/${trainingType}/comment/dislike/${commentId}`, {
                method: 'POST',
                headers: {
                    'Content-Type': 'application/json',
                    'X-CSRF-TOKEN': document.querySelector('meta[name="_csrf"]').getAttribute('content')
                }
            })
                .then(response => response.json())
                .then(data => updateCommentUI(commentId, data))
                .catch(error => console.error('Error:', error));
        });
    });
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
attachLikeDislikeListeners();
