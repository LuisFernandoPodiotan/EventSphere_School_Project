document.addEventListener("DOMContentLoaded", function () {
    // Select all "show/hide password" icons
    const showHidePwIcons = document.querySelectorAll(".showHidePw");

    // Loop through each icon and add a click event listener
    showHidePwIcons.forEach((icon) => {
        icon.addEventListener("click", function () {
            // Find the closest password input field
            const passwordInput = this.closest(".input-field").querySelector(".password");

            // Toggle the password field type between "password" and "text"
            if (passwordInput.type === "password") {
                passwordInput.type = "text";
                this.classList.remove("uil-eye-slash");
                this.classList.add("uil-eye"); // Change icon to "eye" (visible)
            } else {
                passwordInput.type = "password";
                this.classList.remove("uil-eye");
                this.classList.add("uil-eye-slash"); // Change icon back to "eye-slash" (hidden)
            }
        });
    });
});