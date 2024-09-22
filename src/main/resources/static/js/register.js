document.addEventListener('DOMContentLoaded', function () {
    const registerForm = document.querySelector('form');
    registerForm.style.opacity = 0;

    setTimeout(() => {
        registerForm.style.transition = 'opacity 1s ease-in-out';
        registerForm.style.opacity = 1;
    }, 500);

    const submitButton = document.querySelector('button[type="submit"]');
    submitButton.addEventListener('click', function () {
        const password = document.getElementById('password').value;
        const confirmPassword = document.getElementById('passwordConfirm').value;

        if (password !== confirmPassword) {
            alert('Passwords do not match!');
            return false;
        }
    });
});
