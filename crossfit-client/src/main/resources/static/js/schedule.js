document.getElementById('downloadPDF').addEventListener('click', function() {
    const { jsPDF } = window.jspdf;
    const doc = new jsPDF();

    const marginLeft = 14;
    const marginRight = 190;
    const lineHeight = 10;

    doc.setFontSize(18);
    doc.setTextColor(40);
    doc.text('Weekly Training Schedule', marginLeft, 20);

    let y = 30;

    function addTable(day, trainings) {
        // Day Header
        doc.setFontSize(16);
        doc.setTextColor(0);
        doc.text(day, marginLeft, y);
        y += lineHeight;

        // Table Headers
        doc.setFontSize(12);
        doc.setTextColor(100);
        doc.text('Training Type', marginLeft, y);
        doc.text('Date', marginLeft + 40, y);
        doc.text('Coach', marginLeft + 70, y);
        doc.text('Starts at', marginLeft + 105, y);
        doc.text('Free Spots', marginLeft + 140, y);
        doc.text('Joined', marginRight, y);
        y += lineHeight;

        doc.setLineWidth(0.5);
        doc.line(marginLeft, y - 2, marginRight + 20, y - 2); // Header underline

        // Training Data
        trainings.forEach((training, index) => {
            if (index % 2 === 0) {
                doc.setFillColor(240, 240, 240); // Light grey background for even rows
                doc.rect(marginLeft - 2, y - lineHeight + 2, marginRight - marginLeft + 22, lineHeight, 'F');
            }

            doc.setFontSize(10);
            doc.setTextColor(50);
            doc.text(training.trainingType, marginLeft, y);
            doc.text(training.date, marginLeft + 40, y);
            doc.text(training.coach, marginLeft + 70, y);
            doc.text(training.time, marginLeft + 105, y);
            doc.text(training.freeSpots, marginLeft + 140, y);
            doc.text(training.joined, marginRight, y);
            y += lineHeight;

            if (y > 270) {
                doc.addPage();
                y = 20;
                doc.setFontSize(10);
                doc.setTextColor(0);
            }
        });

        y += lineHeight; // Extra space between days
    }

    function getTrainingDataForDay(day) {
        const dayCards = Array.from(document.querySelectorAll('.day-heading')).find(e => e.innerText.includes(day)).parentElement;
        const trainingData = [];

        dayCards.querySelectorAll('.card').forEach(card => {
            trainingData.push({
                trainingType: card.querySelector('.card-title').innerText,
                date: card.querySelector('.card-date').innerText,
                coach: card.querySelector('.coach').innerText,
                time: card.querySelector('.time').innerText,
                freeSpots: card.querySelector('.free-spots').innerText,
                joined: card.querySelector('.text-dark') ? 'Yes' : 'No'
            });
        });

        return trainingData;
    }

    const days = ['Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday'];

    days.forEach(day => {
        const trainings = getTrainingDataForDay(day);
        addTable(day, trainings);
    });

    doc.save('trainings_schedule.pdf');
});
/*<![CDATA[*/
document.addEventListener('DOMContentLoaded', function() {
    var now = new Date();
    var cards = document.querySelectorAll('.card');

    cards.forEach(function(card) {
        var dateString = card.querySelector('.card-date').textContent.trim();
        var timeString = card.querySelector('.time').textContent.trim().substring(14); // Extracting only HH:mm
        var dateTimeString = dateString + ' ' + timeString;
        console.log(dateTimeString);
        var trainingDate = new Date(dateTimeString);


        if (trainingDate < now) {
            var cardBody = card.querySelector('.card-body');
            cardBody.classList.add('unavailable'); // Example class to style past trainings
            // Hide the join button or any other specific content for past trainings
            var joinButton = card.querySelector('.btn-join');
            if (joinButton) {
                joinButton.style.display = 'none';
            }

            var messageDiv = document.createElement('div');
            messageDiv.className = 'text-danger mt-2'; // Add margin-top for spacing
            messageDiv.innerHTML = '<span>This training is in the past</span>';
            cardBody.appendChild(messageDiv);
        }
    });
});
/*]]>*/