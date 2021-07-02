function buildOrdersList(orders) {
  let container = document.createElement("div");
  orders.map((order) => {
    let card = document.createElement("div");
    card.classList.add("order", "card");
    let date = order.date.substring(0, order.date.length - 2).split(" ");
    let idDateText = document.createElement("em");
    idDateText.textContent =
      "ordered on " + date[0] + " at " + date[1] + " - ID: " + order.id;
    idDateText.classList.add("text-muted");
    card.appendChild(idDateText);

    let orderInfo = document.createElement("div");
    orderInfo.classList.add("order-info");
    let subcontainer = document.createElement("div");
    let vendorNameRatingText = document.createElement("h2");
    let vendorName = document.createElement("strong");
    vendorName.textContent = order.vendor.name;
    vendorNameRatingText.appendChild(vendorName);
    let ratingContainer = document.createElement("div");
    for (let r = 0; r < 5; r++) {
      let vendorStar = document.createElement("span");
      vendorStar.classList.add("fa", "fa-star");
      if (r < order.vendor.score) vendorStar.classList.add("checked");
      vendorNameRatingText.appendChild(vendorStar);
    }
    subcontainer.appendChild(vendorNameRatingText);

    let priceTable = document.createElement("table");
    let titles = ["Subtotal", "Shipping", "Total"];
    let values = [
      Intl.NumberFormat("de-DE", {
        style: "currency",
        currency: "EUR",
      }).format(order.total_items_cost),
      Intl.NumberFormat("de-DE", {
        style: "currency",
        currency: "EUR",
      }).format(order.shipping_cost),
      Intl.NumberFormat("de-DE", {
        style: "currency",
        currency: "EUR",
      }).format(order.total_items_cost + order.shipping_cost),
    ];
    [...Array(3).keys()].map((i) => {
      let tr = document.createElement("tr");
      let td1 = document.createElement("td");
      td1.textContent = titles[i];
      let td2 = document.createElement("td");
      td2.textContent = values[i] + "";
      tr.appendChild(td1);
      tr.appendChild(td2);
      priceTable.appendChild(tr);
    });
    subcontainer.appendChild(priceTable);
    orderInfo.appendChild(subcontainer);

    let subcontainer2 = document.createElement("div");
    let itemsTable = document.createElement("table");
    let headersRow = document.createElement("tr");
    titles = ["Quantity", "Item name", "Price"];
    [...Array(3).keys()].map((i) => {
      let th = document.createElement("th");
      th.textContent = titles[i];
      headersRow.appendChild(th);
    });
    itemsTable.appendChild(headersRow);
    order.items.map((item) => {
      let tr = document.createElement("tr");
      let values = [
        item.quantity + "x",
        item.details.name,
        Intl.NumberFormat("de-DE", {
          style: "currency",
          currency: "EUR",
        }).format(item.cost),
      ];
      [...Array(3).keys()].map((i) => {
        let td = document.createElement("td");
        td.textContent = values[i];
        tr.appendChild(td);
      });
      itemsTable.appendChild(tr);
    });
    subcontainer2.appendChild(itemsTable);
    orderInfo.appendChild(subcontainer2);

    card.appendChild(orderInfo);
    container.appendChild(card);
  });
  return container;
}
