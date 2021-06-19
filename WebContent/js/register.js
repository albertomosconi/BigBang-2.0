function showRegister(){
  //if clicked the link for the registration
  //form.style.display = 'none';
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
    if (actualForm.checkValidity) {
      doRequest('register', 'POST', (req) => {
        if (req.readyState == XMLHttpRequest.DONE) {
          var responseBody = req.responseText;
          switch (req.status) {
            case 200:
            //hide the register form
            actualForm.innerHTML = "";
            //secondForm.style.display = 'none';
            //set visible the login form
            var loginForm = restoreLogin();
            document.getElementById('form-box').appendChild(loginForm);
            //var loginForm = document.getElementById('form-box');
            //loginForm.style.display = 'block';
            break;
            default:
              // request failed, display error
              errorContainer.style.display = 'block';
              document.getElementById('errorBody').textContent = responseBody;
              break;
      }
    }

  },
actualForm);
} else{
  form.reportValidity();
}
});
return secondForm;
}

function restoreLogin(){
  var externalBox = document.createElement('div');
  externalBox.classList.add('box');
  externalBox.id = 'box';

  var form = document.createElement('form');
  form.id = "loginForm";
  form.action = "#";

  var email = document.createElement('input');
  email.type = 'text';
  email.placeholder = 'email';
  email.name = 'email';
  email.attributes.required = 'required';
  email.focus();
  form.appendChild(email);

  var space = document.createElement('br');
  form.appendChild(space);

  var psw = document.createElement('input');
  psw.type = 'password';
  psw.placeholder = 'password';
  psw.name = 'psw';
  psw.attributes.required = 'required';
  form.appendChild(psw);

  var space2 = document.createElement('br');
  form.appendChild(space2);

  var logButton = document.createElement('input');
  logButton.type = 'button';
  logButton.value = 'login';
  logButton.classList.add('loginButton');
  logButton.id = 'loginButton';
  form.appendChild(logButton);

  externalBox.appendChild(form);

  var regLink = document.createElement('a');
  regLink.href = '/register';
  regLink.classList.add('register');
  regLink.id = 'register';
  externalBox.appendChild(regLink);

  return externalBox;

}
