/**
 * 
 */
// ===== E-LIBRARY REGISTRATION VALIDATION =====

const form = document.querySelector("form");

const nameInput = document.querySelector('input[name="name"]');

const emailInput = document.querySelector('input[name="email"]');

const passwordInput = document.querySelector('input[name="password"]');


form.addEventListener("submit", function(event) {

    const name = nameInput.value.trim();

    const email = emailInput.value.trim();

    const password = passwordInput.value;


    // Name validation
    if (name.length < 2) {

        alert("Please enter a valid name.");

        nameInput.focus();

        event.preventDefault();

        return;
    }


    // Email validation
    const emailPattern =
        /^[^\s@]+@[^\s@]+\.[^\s@]+$/;


    if (!emailPattern.test(email)) {

        alert("Please enter a valid email address.");

        emailInput.focus();

        event.preventDefault();

        return;
    }


    // Password validation
    if (password.length < 6) {

        alert("Password must contain at least 6 characters.");

        passwordInput.focus();

        event.preventDefault();

        return;
    }


    // Confirmation
    const confirmRegister =
        confirm("Do you want to create your E-Library account?");


    if (!confirmRegister) {

        event.preventDefault();

        return;
    }

});