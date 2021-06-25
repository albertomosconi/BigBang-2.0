function listSearched(items){
  var section = document.createElement('section');

  var keyword = sessionStorage.getItem('keyword');
  var viewed = sessionStorage.getItem('viewItem');

  var infoSearch = document.createElement('h2');
  infoSearch.textContent =
    'Your search for "' + keyword + '" returned ' + items.length + ' results.';
  section.appendChild(infoSearch);

  var itemsContainer = document.createElement('div');
  console.log(`items`, items.length);

if (viewed === null) {
  // no item visualized in the page
  items.forEach((item, i) => {
    var compressedId = document.createElement('div');
    compressedId.id = item['id'];
    var singleContainer = new buildCompressedItem(item);
    compressedId.appendChild(singleContainer);
    itemsContainer.appendChild(compressedId);
  });
}
else{
    //the vield with all id items visualized is not empty
  items.forEach((item, i) => {
    if (viewed.includes(item.id)) {
      //this item is Visualized
      var singleContainer = buildExtendedItem(item);
      itemsContainer.appendChild(singleContainer);
    } else {
      //this one not
      var singleContainer = new buildCompressedItem(item);
      itemsContainer.appendChild(singleContainer);
    }
  });
}
section.appendChild(itemsContainer);
return section;
}

function buildExtendedItemBox(idItem, item){
    //add the itemId visualized in the session sessionStorage
  var visualized = sessionStorage.getItem('viewItem');
  if (visualized == null) {
    visualized = new Array();
  }
  else{
    visualized = JSON.parse("[" + visualized + "]");
  }
  visualized.push(idItem);
  sessionStorage.setItem('viewItem', visualized);

    //now make the item visualized(extended)
  var itemContainer = document.getElementById(idItem);
  itemContainer.innerHTML = "";
  var extendedItem = buildExtendedItem(item);
  itemContainer.appendChild(extendedItem);
}
