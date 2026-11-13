# Hackaton-2  
Domi 🛵
Generation Colombia. Trabajo Colaborativo Grupo N°1 /                                                                                                                                                                                                                                                                        
Integrantes:                                                                                                                                                                                                                              
- David Marca.
- Kendry Vargas G.                                                                                                                   
- Jhonatan Reyes.                                                                                                                                                    
- Leidy González.                                                                                                                       
- Soranny Cuesta.

  Preguntas:

  N°1: ¿Por qué un mapa y no una lista?                                                                                 
  R/= Porque con el HashMap podemos buscar un cliente directamente usando la cédula como llave. Si usáramos una lista tendríamos que recorrerla completa hasta encontrarlo, mientras que con el mapa la búsqueda es     mucho más rápida.
  
  N°2: ¿Qué ventaja tiene que no se repitan?                                                                                
  R/= La ventaja es que evitamos registrar el mismo barrio varias veces. Así la información se mantiene organizada, no se desperdicia memoria y no se generan duplicados innecesarios.
                                                               
  N°3: ¿Por qué el operador nunca escribe los km?                                                                                 
  R/= Porque los kilómetros ya están asociados al barrio dentro del HashMap. Cuando el operador selecciona el barrio, el sistema consulta automáticamente la distancia y evita errores al escribirla manualmente.
  
  N°4: ¿Por qué aquí sí sirve una lista?                                                                               
  R/= Porque normalmente necesitamos recorrer todos los repartidores para revisar quién está disponible, quién puede llevar el pedido o cuál ofrece la mejor opción. Para eso una lista funciona muy bien.
  
  N°5: ¿Cómo encuentran un pedido por su número?                                                                                 
  R/= Los pedidos se guardan en una lista y para encontrar uno específico se recorre la colección comparando su etiqueta o número de pedido hasta localizar el correcto.
  
