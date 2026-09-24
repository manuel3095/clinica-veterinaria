/**
 * Pruebas de las validaciones del formulario de mascotas (front-end).
 *
 * Ejercita directamente validaciones.js con casos positivos y
 * negativos para las cinco categorias exigidas por la evidencia,
 * en espejo de PruebasValidaciones.java (backend). Se ejecuta con
 * Node.js puro, sin dependencias externas:
 *
 *   node pruebas-validaciones.js
 */
const ValidacionesMascota = require("./validaciones.js");

let casosTotales = 0;
let casosAprobados = 0;

function caso(descripcion, mascota, debeSerValido) {
    casosTotales++;
    const errores = ValidacionesMascota.validarMascota(mascota);
    const esValido = errores.length === 0;
    const aprobado = esValido === debeSerValido;
    if (aprobado) casosAprobados++;
    console.log((aprobado ? "[APROBADO] " : "[FALLIDO]  ") + descripcion);
    if (!esValido) {
        errores.forEach((e) => console.log("           -> " + e));
    }
}

const base = {
    nomMascota: "Max",
    estado: "Estable",
    especie: "Canis lupus familiaris",
    edad: "3",
    raza: "Husky",
    fechaIngreso: "2023-05-15",
    correoPropietario: "propietario@email.com"
};

console.log("=========================================================");
console.log(" PRUEBAS DE VALIDACION (FRONT-END) - Modulo de Mascotas");
console.log("=========================================================\n");

console.log("--- Categoria: TEXTOS (nombre/especie) ---");
caso("Nombre y especie validos", { ...base }, true);
caso("Nombre con numeros ('Max2') debe rechazarse", { ...base, nomMascota: "Max2" }, false);
caso("Nombre vacio debe rechazarse", { ...base, nomMascota: "" }, false);
caso("Estado fuera del dominio permitido ('Perdido') debe rechazarse", { ...base, estado: "Perdido" }, false);

console.log("\n--- Categoria: CARACTERES ESPECIALES (correo del propietario) ---");
caso("Correo del propietario valido", { ...base, correoPropietario: "duena@email.com" }, true);
caso("Correo sin arroba debe rechazarse", { ...base, correoPropietario: "duenaemail.com" }, false);
caso("Correo con caracteres invalidos debe rechazarse", { ...base, correoPropietario: "duena@@ema#il.com" }, false);

console.log("\n--- Categoria: NUMEROS (edad) ---");
caso("Edad valida ('4')", { ...base, edad: "4" }, true);
caso("Edad con letras ('4a') debe rechazarse", { ...base, edad: "4a" }, false);
caso("Edad fuera de rango ('99') debe rechazarse", { ...base, edad: "99" }, false);

console.log("\n--- Categoria: FECHAS (fecha de ingreso) ---");
caso("Fecha de ingreso valida (2023-05-15)", { ...base, fechaIngreso: "2023-05-15" }, true);
const manana = new Date();
manana.setDate(manana.getDate() + 1);
caso("Fecha de ingreso futura debe rechazarse",
    { ...base, fechaIngreso: manana.toISOString().slice(0, 10) }, false);
caso("Fecha de ingreso hace 50 anios (fuera de rango) debe rechazarse",
    { ...base, fechaIngreso: "1975-01-01" }, false);

console.log("\n--- Categoria: LONGITUDES (raza) ---");
caso("Raza con longitud valida ('Golden Retriever')", { ...base, raza: "Golden Retriever" }, true);
caso("Raza demasiado corta ('S') debe rechazarse", { ...base, raza: "S" }, false);

console.log("\n=========================================================");
console.log(" RESULTADO FINAL: " + casosAprobados + " / " + casosTotales + " casos aprobados");
console.log("=========================================================");

if (casosAprobados !== casosTotales) {
    process.exit(1);
}
