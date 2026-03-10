const statusPanel = document.getElementById("status");

const clientSelect = document.getElementById("client-select");
const supplierSelect = document.getElementById("supplier-select");
const deliverySupplierSelect = document.getElementById("delivery-supplier-select");
const carSelect = document.getElementById("car-select");
const mechanicSelect = document.getElementById("mechanic-select");
const partSelect = document.getElementById("part-select");
const deliveryPartSelect = document.getElementById("delivery-part-select");

const ordersList = document.getElementById("orders-list");
const partsList = document.getElementById("parts-list");

function log(message, level = "info") {
    const stamp = new Date().toLocaleTimeString();
    const symbol = level === "error" ? "ERR" : "OK ";
    statusPanel.textContent = `[${stamp}] ${symbol} ${message}\n${statusPanel.textContent}`.trim();
}

async function api(path, options = {}) {
    const response = await fetch(path, {
        headers: {"Content-Type": "application/json"},
        ...options
    });

    if (!response.ok) {
        let message = `HTTP ${response.status}`;
        try {
            const body = await response.json();
            if (body.error) {
                message = body.error;
            }
        } catch (_) {
            // no body
        }
        throw new Error(message);
    }

    if (response.status === 204) {
        return null;
    }

    return response.json();
}

function setOptions(selectElement, values, labelBuilder) {
    selectElement.innerHTML = "";
    if (values.length === 0) {
        const option = document.createElement("option");
        option.value = "";
        option.textContent = "No data available";
        selectElement.appendChild(option);
        return;
    }

    values.forEach(item => {
        const option = document.createElement("option");
        option.value = item.id;
        option.textContent = labelBuilder(item);
        selectElement.appendChild(option);
    });
}

function fromForm(form) {
    const formData = new FormData(form);
    return Object.fromEntries(formData.entries());
}

async function refreshClients() {
    const clients = await api("/api/clients");
    setOptions(clientSelect, clients, c => `${c.firstName} ${c.lastName} (${c.email})`);
}

async function refreshSuppliers() {
    const suppliers = await api("/api/suppliers");
    setOptions(supplierSelect, suppliers, s => `${s.name}`);
    setOptions(deliverySupplierSelect, suppliers, s => `${s.name}`);
}

async function refreshCars() {
    const clients = await api("/api/clients");
    const cars = [];
    for (const client of clients) {
        const ownedCars = await api(`/api/cars/by-client/${client.id}`);
        cars.push(...ownedCars);
    }
    setOptions(carSelect, cars, c => `${c.brand} ${c.model} [${c.plateNumber}]`);
}

async function refreshMechanics() {
    const mechanics = await api("/api/mechanics");
    setOptions(mechanicSelect, mechanics, m => `${m.firstName} ${m.lastName}`);
}

async function refreshParts() {
    const parts = await api("/api/parts");
    setOptions(partSelect, parts, p => `${p.name} | stock: ${p.availableStock}`);
    setOptions(deliveryPartSelect, parts, p => `${p.name} | stock: ${p.availableStock}`);

    partsList.innerHTML = "";
    parts.forEach(part => {
        const item = document.createElement("div");
        item.className = "list-item";
        item.innerHTML = `
            <strong>${part.name}</strong>
            <p>Stock: ${part.availableStock}</p>
            <p>Unit Price: ${part.unitPrice}</p>
            <p>Supplier ID: ${part.supplierId}</p>
        `;
        partsList.appendChild(item);
    });
}

async function refreshOrders() {
    const orders = await api("/api/service-orders?status=IN_PROGRESS");
    const completed = await api("/api/service-orders?status=COMPLETED");
    const allOrders = [...orders, ...completed];

    ordersList.innerHTML = "";
    allOrders.forEach(order => {
        const item = document.createElement("div");
        item.className = "list-item";
        const statusClass = order.status === "COMPLETED" ? "ok" : "warn";
        item.innerHTML = `
            <strong>${order.serviceName}</strong>
            <p>Order ID: ${order.id}</p>
            <p>Total cost: ${order.totalCost}</p>
            <p>Scheduled: ${order.scheduledAt}</p>
            <span class="status-inline ${statusClass}">${order.status}</span>
        `;

        if (order.status === "IN_PROGRESS") {
            const completeButton = document.createElement("button");
            completeButton.type = "button";
            completeButton.textContent = "Complete Order";
            completeButton.addEventListener("click", async () => {
                try {
                    await api(`/api/service-orders/${order.id}/complete`, {method: "PATCH"});
                    log(`Order ${order.id} completed`);
                    await refreshOrders();
                } catch (error) {
                    log(error.message, "error");
                }
            });
            item.appendChild(completeButton);
        }

        ordersList.appendChild(item);
    });
}

document.getElementById("client-form").addEventListener("submit", async event => {
    event.preventDefault();
    const body = fromForm(event.target);
    try {
        await api("/api/clients", {method: "POST", body: JSON.stringify(body)});
        event.target.reset();
        log("Client created");
        await refreshClients();
    } catch (error) {
        log(error.message, "error");
    }
});

document.getElementById("car-form").addEventListener("submit", async event => {
    event.preventDefault();
    const body = fromForm(event.target);
    body.fabricationYear = Number(body.fabricationYear);
    try {
        await api("/api/cars", {method: "POST", body: JSON.stringify(body)});
        event.target.reset();
        log("Car created");
        await refreshCars();
    } catch (error) {
        log(error.message, "error");
    }
});

document.getElementById("mechanic-form").addEventListener("submit", async event => {
    event.preventDefault();
    const body = fromForm(event.target);
    try {
        await api("/api/mechanics", {method: "POST", body: JSON.stringify(body)});
        event.target.reset();
        log("Mechanic created");
        await refreshMechanics();
    } catch (error) {
        log(error.message, "error");
    }
});

document.getElementById("supplier-form").addEventListener("submit", async event => {
    event.preventDefault();
    const body = fromForm(event.target);
    try {
        await api("/api/suppliers", {method: "POST", body: JSON.stringify(body)});
        event.target.reset();
        log("Supplier created");
        await refreshSuppliers();
    } catch (error) {
        log(error.message, "error");
    }
});

document.getElementById("part-form").addEventListener("submit", async event => {
    event.preventDefault();
    const body = fromForm(event.target);
    body.availableStock = Number(body.availableStock);
    body.unitPrice = Number(body.unitPrice);
    try {
        await api("/api/parts", {method: "POST", body: JSON.stringify(body)});
        event.target.reset();
        log("Part created");
        await refreshParts();
    } catch (error) {
        log(error.message, "error");
    }
});

document.getElementById("order-form").addEventListener("submit", async event => {
    event.preventDefault();
    const body = fromForm(event.target);
    const payload = {
        carId: body.carId,
        mechanicId: body.mechanicId,
        serviceName: body.serviceName,
        description: body.description,
        laborCost: Number(body.laborCost),
        scheduledAt: new Date(body.scheduledAt).toISOString().slice(0, 19),
        requiredParts: [{partId: body.partId, quantity: Number(body.quantity)}]
    };

    try {
        await api("/api/service-orders", {method: "POST", body: JSON.stringify(payload)});
        event.target.reset();
        log("Service order created and stock updated");
        await Promise.all([refreshOrders(), refreshParts()]);
    } catch (error) {
        log(error.message, "error");
    }
});

document.getElementById("delivery-form").addEventListener("submit", async event => {
    event.preventDefault();
    const body = fromForm(event.target);
    const payload = {
        partId: body.partId,
        supplierId: body.supplierId,
        quantity: Number(body.quantity),
        deliveredAt: new Date(body.deliveredAt).toISOString().slice(0, 19)
    };

    try {
        await api("/api/deliveries/receive", {method: "PATCH", body: JSON.stringify(payload)});
        event.target.reset();
        log("Delivery recorded and stock increased");
        await refreshParts();
    } catch (error) {
        log(error.message, "error");
    }
});

document.getElementById("refresh-orders").addEventListener("click", () => refreshOrders());
document.getElementById("refresh-parts").addEventListener("click", () => refreshParts());

async function bootstrap() {
    try {
        await Promise.all([
            refreshClients(),
            refreshSuppliers(),
            refreshCars(),
            refreshMechanics(),
            refreshParts(),
            refreshOrders()
        ]);
        log("Dashboard loaded");
    } catch (error) {
        log(`Startup failed: ${error.message}`, "error");
    }
}

bootstrap();
