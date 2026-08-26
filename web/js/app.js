// La web consume la MISMA API REST (modulo backend) que usa el sistema de escritorio.
const API_BASE = "http://localhost:8080/api";

const vuelosGrid = document.getElementById("vuelos-grid");
const vuelosEstado = document.getElementById("vuelos-estado");
const filtroContenedor = document.getElementById("rutas");

let todosLosVuelos = [];
let destinoActivo = "TODAS";
let vueloSeleccionado = null;

async function cargarVuelos() {
  vuelosEstado.textContent = "Cargando vuelos disponibles...";
  try {
    const res = await fetch(`${API_BASE}/vuelos`);
    if (!res.ok) throw new Error("Respuesta no valida del servidor");
    todosLosVuelos = await res.json();
    vuelosEstado.textContent = "";
    construirFiltros();
    renderVuelos();
  } catch (err) {
    vuelosEstado.textContent =
      "No se pudo conectar con el sistema de reservas. Verifica que el backend (Spring Boot) este corriendo en localhost:8080.";
  }
}

function construirFiltros() {
  const ciudades = new Set(
    todosLosVuelos
      .filter((v) => v.destino)
      .map((v) => v.destino.ciudad)
  );

  // Conserva el chip "Todos los destinos" y agrega uno por cada ciudad destino
  filtroContenedor.querySelectorAll(".chip:not([data-destino='TODAS'])").forEach((c) => c.remove());
  ciudades.forEach((ciudad) => {
    const btn = document.createElement("button");
    btn.className = "chip";
    btn.dataset.destino = ciudad;
    btn.textContent = ciudad;
    btn.addEventListener("click", () => seleccionarFiltro(btn));
    filtroContenedor.appendChild(btn);
  });

  filtroContenedor.querySelector("[data-destino='TODAS']").addEventListener("click", (e) => seleccionarFiltro(e.target));
}

function seleccionarFiltro(btn) {
  filtroContenedor.querySelectorAll(".chip").forEach((c) => c.classList.remove("active"));
  btn.classList.add("active");
  destinoActivo = btn.dataset.destino;
  renderVuelos();
}

function renderVuelos() {
  const lista =
    destinoActivo === "TODAS"
      ? todosLosVuelos
      : todosLosVuelos.filter((v) => v.destino && v.destino.ciudad === destinoActivo);

  vuelosGrid.innerHTML = "";

  if (lista.length === 0) {
    vuelosGrid.innerHTML = `<p style="color:var(--texto-suave)">No hay vuelos disponibles para este destino por el momento.</p>`;
    return;
  }

  lista.forEach((vuelo) => {
    const origenCiudad = vuelo.origen ? vuelo.origen.ciudad : "?";
    const origenCod = vuelo.origen ? vuelo.origen.codigoIata : "";
    const destinoCiudad = vuelo.destino ? vuelo.destino.ciudad : "?";
    const destinoCod = vuelo.destino ? vuelo.destino.codigoIata : "";
    const sinCupo = vuelo.asientosDisponibles <= 0;

    const card = document.createElement("div");
    card.className = "vuelo-card";
    card.innerHTML = `
      <div class="vuelo-card-header">
        <span>${vuelo.numeroVuelo}</span>
        <span class="estado-badge">${vuelo.estado || "PROGRAMADO"}</span>
      </div>
      <div class="vuelo-card-body">
        <div class="ruta-visual">
          <div>
            <div class="ruta-ciudad">${origenCod}</div>
            <div class="ruta-codigo">${origenCiudad}</div>
          </div>
          <div class="ruta-linea"></div>
          <div style="text-align:right">
            <div class="ruta-ciudad">${destinoCod}</div>
            <div class="ruta-codigo">${destinoCiudad}</div>
          </div>
        </div>
        <p class="vuelo-meta">
          <strong>${vuelo.fechaSalida || ""}</strong> · Salida ${vuelo.horaSalida || "-"} · Llegada ${vuelo.horaLlegada || "-"}<br>
          ${sinCupo ? "Sin asientos disponibles" : vuelo.asientosDisponibles + " asientos disponibles"}
        </p>
        <div class="vuelo-footer">
          <div class="vuelo-precio">S/ ${Number(vuelo.precio).toFixed(0)} <span>/ pasaje</span></div>
          <button class="btn-reservar" data-id="${vuelo.id}" ${sinCupo ? "disabled" : ""}>
            ${sinCupo ? "Agotado" : "Reservar"}
          </button>
        </div>
      </div>
    `;
    vuelosGrid.appendChild(card);
  });

  vuelosGrid.querySelectorAll(".btn-reservar:not(:disabled)").forEach((btn) => {
    btn.addEventListener("click", () => {
      const id = Number(btn.dataset.id);
      vueloSeleccionado = todosLosVuelos.find((v) => v.id === id);
      abrirModal(vueloSeleccionado);
    });
  });
}

// --- Modal de reserva ---
const overlay = document.getElementById("modal-overlay");
const modalNombre = document.getElementById("modal-vuelo-nombre");
const modalPrecio = document.getElementById("modal-vuelo-precio");
const form = document.getElementById("reserva-form");
const formMensaje = document.getElementById("form-mensaje");

function abrirModal(vuelo) {
  const origenCod = vuelo.origen ? vuelo.origen.codigoIata : "?";
  const destinoCod = vuelo.destino ? vuelo.destino.codigoIata : "?";
  modalNombre.textContent = `${vuelo.numeroVuelo} · ${origenCod} → ${destinoCod}`;
  modalPrecio.textContent = `S/ ${Number(vuelo.precio).toFixed(0)} por pasaje`;
  formMensaje.textContent = "";
  formMensaje.className = "form-mensaje";
  form.reset();
  overlay.classList.add("open");
}

function cerrarModal() {
  overlay.classList.remove("open");
}

document.getElementById("modal-close").addEventListener("click", cerrarModal);
overlay.addEventListener("click", (e) => {
  if (e.target === overlay) cerrarModal();
});

form.addEventListener("submit", async (e) => {
  e.preventDefault();
  if (!vueloSeleccionado) return;

  const formData = new FormData(form);
  const pasajeroData = {
    nombres: formData.get("nombres"),
    apellidos: formData.get("apellidos"),
    documento: formData.get("documento"),
    email: formData.get("email"),
    telefono: formData.get("telefono"),
  };
  const numPasajes = Number(formData.get("numPasajes"));

  formMensaje.textContent = "Procesando reserva...";
  formMensaje.className = "form-mensaje";

  try {
    // 1) Registrar al pasajero
    const pasajeroRes = await fetch(`${API_BASE}/pasajeros`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(pasajeroData),
    });
    if (!pasajeroRes.ok) throw new Error("No se pudo registrar el pasajero");
    const pasajero = await pasajeroRes.json();

    // 2) Crear la reserva asociada al pasajero y al vuelo elegido
    const reservaRes = await fetch(`${API_BASE}/reservas`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        pasajero: { id: pasajero.id },
        vuelo: { id: vueloSeleccionado.id },
        numPasajes,
        estado: "PENDIENTE",
      }),
    });
    if (!reservaRes.ok) throw new Error("No se pudo registrar la reserva");

    formMensaje.textContent = "Reserva registrada. Te contactaremos para confirmar el pago.";
    formMensaje.className = "form-mensaje ok";
    setTimeout(cerrarModal, 2200);
  } catch (err) {
    formMensaje.textContent = "Ocurrio un error al procesar tu reserva. Intenta nuevamente.";
    formMensaje.className = "form-mensaje error";
  }
});

cargarVuelos();
