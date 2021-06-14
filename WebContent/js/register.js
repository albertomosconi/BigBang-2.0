function showRegister(){
  //if clicked the link for the registration
  form.style.display = 'none';
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
    //surname
  var surname = document.createElement('input');
  surname.type = 'text';
  surname.placeholder = 'surname';
  surname.name = 'surname';
    //email
  var email = document.createElement('input');
  email.type = 'email';
  email.placeholder = 'email';
  email.name = 'email';
    //password1
  var psw = document.createElement('input');
  psw.type = 'password';
  psw.placeholder = 'password';
  psw.name = 'psw';
    //password2
  var confirmPsw = document.createElement('input');
  confirmPsw.type = 'password';
  confirmPsw.placeholder = 'password';
  confirmPsw.name = 'confirmPwd';
    //address
  var address = document.createElement('input');
  address.type = 'text';
  address.placeholder = 'address';
  address.name = 'address';
    //button
  var registerButton = document.createElement('input');
  registerButton.type = 'submit';
  registerButton.classList.add("registerButton");

  actualForm.appendChild(name);
  actualForm.appendChild(surname);
  actualForm.appendChild(email);
  actualForm.appendChild(psw);
  actualForm.appendChild(confirmPsw);
  actualForm.appendChild(address);
  actualForm.appendChild(registerButton);

  formContainer.appendChild(actualForm);

  secondForm.appendChild(formContainer);


  document.getElementById('registerButton').addEventListener('click', (e) =>{
    e.preventDefault();
    if (actualForm.checkValidity) {
      doRequest('register', 'POST', (req) => {
        if (req.readyState == XMLHttpRequest.DONE) {
          var responseBody = req.responseText;
          switch (req.status) {
            case 200:
            //hide the register form
            secondForm.style.display = 'none';
            //set visible the login form
            form.style.display = 'block';
            break;
            default:
              // request failed, display error
              errorContainer.style.display = 'block';
              document.getElementById('errorBody').textContent = responseBody;
              break;
      }
    }

  },
form);
} else{
  form.reportValidity();
}
});
}
