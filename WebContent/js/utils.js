function doRequest(url, method, form, callback) {
  var request = new XMLHttpRequest();
  request.onreadystatechange = () => callback(request);
  request.open(method, url);
  if (form == null) request.send();
  else {
    var fd = new FormData(form);
    request.send(fd);
  }
}
