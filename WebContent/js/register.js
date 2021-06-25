function buildRegister(){
  //if clicked the link for the registration
  var secondForm = document.createElement('div');
  secondForm.classList.add('registerForm');
  var formContainer = document.createElement('div');
  formContainer.classList.add('formContainer');
  var actualForm = document.createElement('form');
  actualForm.id = 'registerForm';
    //name
  var name = document.createElement('input');
  name.type = 'text';
  name.placeholder = 'name';
  name.name = 'name';
  name.required = 'required';
    //surname
  var surname = document.createElement('input');
  surname.type = 'text';
  surname.placeholder = 'surname';
  surname.name = 'surname';
  surname.required = 'required';
    //email
  var email = document.createElement('input');
  email.type = 'email';
  email.placeholder = 'email';
  email.name = 'email';
  email.required = 'required';
    //password1
  var psw = document.createElement('input');
  psw.type = 'password';
  psw.placeholder = 'password';
  psw.name = 'psw';
  psw.required = 'required';
    //password2
  var confirmPsw = document.createElement('input');
  confirmPsw.type = 'password';
  confirmPsw.placeholder = 'password';
  confirmPsw.name = 'confirmPwd';
  confirmPsw.required = 'required';
    //address
  var address = document.createElement('input');
  address.type = 'text';
  address.placeholder = 'address';
  address.name = 'address';
  address.required = 'required';
    //button
  var registerButton = document.createElement('button');
  registerButton.type = 'submit';
  registerButton.classList.add("registerButton");
  registerButton.textContent = 'Register';
  registerButton.id = 'registerButton';
    //space between inputs
  var space1 = document.createElement('br');
  var space2 = document.createElement('br');
  var space3 = document.createElement('br');
  var space4 = document.createElement('br');
  var space5 = document.createElement('br');
  var space6 = document.createElement('br');

  actualForm.appendChild(name);
  actualForm.appendChild(space1);
  actualForm.appendChild(surname);
  actualForm.appendChild(space2);
  actualForm.appendChild(email);
  actualForm.appendChild(space3);
  actualForm.appendChild(psw);
  actualForm.appendChild(space4);
  actualForm.appendChild(confirmPsw);
  actualForm.appendChild(space5);
  actualForm.appendChild(address);
  actualForm.appendChild(space6);
  actualForm.appendChild(registerButton);

  formContainer.appendChild(actualForm);

  secondForm.appendChild(formContainer);


  registerButton.addEventListener('click', (e) =>{
    e.preventDefault();
    if ((actualForm.checkValidity()) && (psw.value === confirmPsw.value)) {
      doRequest('register', 'POST', (req) => {
        if (req.readyState == XMLHttpRequest.DONE) {
          var responseBody = req.responseText;
          switch (req.status) {
            case 200:
            // remove the error msg create bofore (if exist)
            var errorContainer = document.getElementById('errorMessage');
            if (errorContainer != null) {
                errorContainer.innerHTML = '';
            }
              //restore the login form
              var loginForm = document.getElementById('form-box');
              loginForm.style.display = 'block';
              break;

            default:
              // request failed, display error
              var errorContainer = document.getElementById('errorMessage');
              errorContainer.style.display = 'block';
              document.getElementById('errorBody').textContent = responseBody;
              break;
      }
    }
  },
actualForm);
} else{
  actualForm.reportValidity();
  if (psw.value !== confirmPsw.value) {
    //show error msg
  }
}
});
return secondForm;
}
