document.addEventListener('DOMContentLoaded', function () {
    const loginForm = document.querySelector('section');
    loginForm.style.opacity = 0;

    setTimeout(() => {
        loginForm.style.transition = 'opacity 1s ease-in-out';
        loginForm.style.opacity = 1;
    }, 500);
});
