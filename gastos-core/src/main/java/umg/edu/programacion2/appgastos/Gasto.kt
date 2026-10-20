package umg.edu.programacion2.appgastos

class Gasto(
    categoriaGasto: CategoriaGasto,
    descripcion: String,
    monto: Double
) : MovimientoBase(
    categoria = categoriaGasto.etiqueta,
    descripcion = descripcion,
    monto = monto
)