package umg.edu.programacion2.appgastos

class Ingreso(
    categoriaIngreso: CategoriaIngreso,
    descripcion: String,
    monto: Double
) : MovimientoBase(
    categoria = categoriaIngreso.etiqueta,
    descripcion = descripcion,
    monto = monto
)