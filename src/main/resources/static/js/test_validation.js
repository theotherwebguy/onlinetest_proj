document.addEventListener('DOMContentLoaded', function () {
    const form = document.querySelector('form');  // Get the form element
    const questions = document.querySelectorAll('.question');  // Get all the questions
    const remainingCountElement = document.getElementById('remaining-count');  // Get the element that displays remaining questions

    console.log("Loaded test with", questions.length, "questions.");

    // Initialize the remaining questions count
    let remainingCount = questions.length;
    remainingCountElement.textContent = remainingCount;  // Display the remaining count

    // Function to update the remaining questions count
    function updateRemainingCount() {
        remainingCount = questions.length;

        // Check if each question is answered
        questions.forEach(function (question) {
            if (question.querySelector('input[type="radio"]:checked')) {
                remainingCount--;
            }
        });

        console.log("Remaining unanswered questions:", remainingCount);

        // Update the display with the new count
        remainingCountElement.textContent = remainingCount;
    }

    // Attach event listeners to all radio buttons to detect answer selection
    questions.forEach(function (question) {
        question.querySelectorAll('input[type="radio"]').forEach(function (radio) {
            radio.addEventListener('change', updateRemainingCount);  // Update count when an answer is selected
        });
    });

    // Form submission handling
    form.addEventListener('submit', function (event) {
        if (remainingCount > 0) {
            event.preventDefault();
            alert('Please answer all questions before submitting the test.');
        } else {
            questions.forEach(function (question) {
                const selectedAnswer = question.querySelector('input[type="radio"]:checked');

                // Adjust the index of selected answer before submission (add 1)
                if (selectedAnswer) {
                    selectedAnswer.value = parseInt(selectedAnswer.value) + 1;
                }
            });
        }
    });
});
