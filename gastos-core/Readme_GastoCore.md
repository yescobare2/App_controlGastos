# gasto-core (Núcleo y Lógica de Negocio)



Este módulo es el corazón independiente de la aplicación. Su objetivo principal es encapsular todas las reglas financieras, modelos de dominio y la persistencia de datos, permitiendo que la lógica de negocio esté completamente desacoplada de cualquier interfaz de usuario (sea móvil, web o de escritorio).



#### Modelos de Dominio (/model):



Transaction: Representa cada movimiento financiero individual. Contiene atributos críticos como el identificador único (id), el monto (amount), el tipo de movimiento (TransactionType: INGRESO o EGRESO), la fecha y hora de registro (timestamp), una nota o descripción opcional, y la relación con una categoría y cuenta.



Category: Define la clasificación de los gastos e ingresos (por ejemplo: Alimentación, Transporte, Servicios Básicos, Salario). Incluye propiedades como el nombre, un icono representativo y un indicador visual/color.



Budget: Estructura destinada a controlar los límites de gasto por categoría o periodo (mensual/quincenal), almacenando el monto límite asignado y el estado actual de consumo.



#### Gestión y Procesamiento de Transacciones (/service/TransactionService):



Controla el ciclo de vida completo de un movimiento financiero (creación, lectura, actualización y eliminación).



Implementa validaciones lógicas estrictas, tales como verificar que los montos sean mayores a cero, validar la existencia de la categoría asociada y asegurar que la fecha de la transacción no sea inválida.



Ofrece métodos de filtrado avanzado por rangos de fechas (inicio y fin), por tipo de transacción (solo ingresos o solo gastos) y por categorías específicas.



#### Control y Alertas de Presupuestos (/service/BudgetService):



Calcula de manera dinámica el gasto acumulado en una categoría durante un periodo de tiempo determinado.



Evalúa si el total gastado supera el límite configurado en el Budget, disparando alertas lógicas o estados de advertencia para que las capas superiores puedan notificar al usuario en tiempo real.



#### Cálculos Financieros y Reportes (/service/ReportService):



Procesa colecciones masivas de transacciones para calcular indicadores clave como el balance general neto (ingresos menos egresos totales).



Agrupa los movimientos por categoría para estructurar datos listos para ser consumidos por gráficos estadísticos (porcentajes de participación de cada gasto sobre el total).



Genera resúmenes periódicos (diarios, semanales o mensuales) para el análisis del comportamiento financiero del usuario.

#### 

#### Capa de Datos y Persistencia (/data):



Administra la conexión con la base de datos local ( SQLite / Room / JPA ) a través de objetos de acceso a datos (DAOs).



Mapea las entidades de la base de datos hacia los modelos de dominio puros mediante funciones de extensión o mappers, garantizando que el núcleo permanezca independiente de la tecnología específica de almacenamiento.

