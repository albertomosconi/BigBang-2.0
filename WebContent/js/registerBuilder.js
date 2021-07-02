function buildRegister() {
  let image = document.getElementById("loginImg");
  image.classList.remove("loginImg");
  image.classList.add("registerImg");
  let mainContainer = document.getElementById("mainContainer");
  mainContainer.removeChild(image);
  mainContainer.appendChild(image);
  //if clicked the link for the registration
  var secondForm = document.createElement("div");
  secondForm.classList.add("registerForm");
  var formContainer = document.createElement("div");
  formContainer.classList.add("formContainer");
  var actualForm = document.createElement("form");
  actualForm.id = "registerForm";
  //name
  var name = document.createElement("input");
  name.type = "text";
  name.placeholder = "name";
  name.name = "name";
  name.required = "required";
  //surname
  var surname = document.createElement("input");
  surname.type = "text";
  surname.placeholder = "surname";
  surname.name = "surname";
  surname.required = "required";
  //email
  var email = document.createElement("input");
  email.type = "email";
  email.placeholder = "email";
  email.name = "email";
  email.required = "required";
  //password1
  var psw = document.createElement("input");
  psw.type = "password";
  psw.placeholder = "password";
  psw.name = "psw";
  psw.required = "required";
  //password2
  var confirmPsw = document.createElement("input");
  confirmPsw.type = "password";
  confirmPsw.placeholder = "password";
  confirmPsw.name = "confirmPwd";
  confirmPsw.required = "required";
  //address
  var address = document.createElement("input");
  address.type = "text";
  address.placeholder = "address";
  address.name = "address";
  address.required = "required";
  //button
  var registerButton = document.createElement("button");
  registerButton.type = "submit";
  registerButton.classList.add("loginButton");
  registerButton.textContent = "Register";
  registerButton.id = "registerButton";
  // link to login
  let loginLink = document.createElement("a");
  loginLink.classList.add("register");
  loginLink.id = "login";
  loginLink.textContent = "login";
  loginLink.addEventListener("click", (e) => {
    e.preventDefault();
    // remove the error msg create bofore (if exist)
    var errorContainer = document.getElementById("errorMessage");
    errorContainer.style.display = "none";

    //restore the login form
    let image = document.getElementById("loginImg");
    image.classList.remove("registerImg");
    image.classList.add("loginImg");
    let mainContainer = document.getElementById("mainContainer");
    mainContainer.removeChild(image);
    mainContainer.prepend(image);
    var loginForm = document.getElementById("form-box");
    loginForm.style.display = "block";
    let registerForm = document.getElementById("registerForm");
    registerForm.remove();
  });

  //space between inputs
  var space1 = document.createElement("br");
  var space2 = document.createElement("br");
  var space3 = document.createElement("br");
  var space4 = document.createElement("br");
  var space5 = document.createElement("br");
  var space6 = document.createElement("br");
  var space7 = document.createElement("br");

  actualForm.append(
    name,
    space1,
    surname,
    space2,
    email,
    space3,
    psw,
    space4,
    confirmPsw,
    space5,
    address,
    space6,
    registerButton,
    space7,
    loginLink
  );

  formContainer.appendChild(actualForm);

  secondForm.appendChild(formContainer);

  registerButton.addEventListener("click", (e) => {
    e.preventDefault();
    if (actualForm.checkValidity() && psw.value === confirmPsw.value) {
      doRequest(
        "register",
        "POST",
        (req) => {
          if (req.readyState == XMLHttpRequest.DONE) {
            var responseBody = req.responseText;
            switch (req.status) {
              case 200:
                // remove the error msg create bofore (if exist)
                var errorContainer = document.getElementById("errorMessage");
                errorContainer.style.display = "none";

                //restore the login form
                let image = document.getElementById("loginImg");
                image.classList.remove("registerImg");
                image.classList.add("loginImg");
                let mainContainer = document.getElementById("mainContainer");
                mainContainer.removeChild(image);
                mainContainer.prepend(image);
                var loginForm = document.getElementById("form-box");
                loginForm.style.display = "block";
                let registerForm = document.getElementById("registerForm");
                registerForm.remove();
                break;

              default:
                // request failed, display error
                var errorContainer = document.getElementById("errorMessage");
                errorContainer.style.display = "block";
                document.getElementById("errorBody").textContent = responseBody;
                break;
            }
          }
        },
        actualForm
      );
    } else {
      actualForm.reportValidity();
      if (psw.value !== confirmPsw.value) {
        //show error msg
        let errorContainer = document.getElementById("errorMessage");
        errorContainer.style.display = "block";
         document.getElementById("errorBody").textContent = "The confirm password do not match the password";
      }
    }
  });
  return secondForm;
}
