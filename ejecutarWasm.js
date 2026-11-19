
const fs = require("fs");

const archivo = "SalidaTP4/Programa.wasm";
const bytes = fs.readFileSync(archivo);

const imports = {
    console: {
        log_i32: (valor) => console.log(valor),
        log_f64: (valor) => console.log(valor),
        error_overflow: () => {
            console.error("ERROR: Overflow aritmético.");
        }
    }
};

WebAssembly.instantiate(bytes, imports)
    .then(({ instance }) => {
        console.log("Ejecutando WebAssembly...\n");
        instance.exports.main();
        console.log("\nEjecución finalizada correctamente.");
    })
    .catch((error) => {
        console.error("\nError durante la ejecución WebAssembly:");
        console.error(error);
    });
