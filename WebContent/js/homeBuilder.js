function buildLastViewed(items) {
  var container = document.createElement('div');

  items.forEach((item, i) => {
    container.appendChild(buildExtendedItem(item));
  });
  return container;
}
