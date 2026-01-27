package com.example.esgrima.data

const val JSON_ARBITROS_RAW = """
[{
  "firstName": "Juan", "lastName": "Ramirez", "gender": "masculino", "age": 24, "federateNumber": 2001, "club": "Club de Esgrima Valencia", "country": "España", "modality": ["espada", "sable", "florete"], "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Juan", "lastName": "Ramos", "gender": "masculino", "age": 32, "federateNumber": 2317, "club": "Club de Esgrima Valencia", "country": "España", "modality": ["espada", "sable", "florete"], "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Maria", "lastName": "Ramirez", "gender": "masculino", "age": 18, "federateNumber": 2101, "club": "Club de Esgrima Alicante", "country": "España", "modality": ["espada", "sable", "florete"], "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Juan", "lastName": "Rodriguez", "gender": "masculino", "age": 45, "federateNumber": 1015, "club": "Club de Esgrima Alicante", "country": "España", "modality": ["sable", "florete"], "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Juan", "lastName": "Random", "gender": "masculino", "age": 42, "federateNumber": 1021, "club": "Club de Esgrima Mislata", "country": "España", "modality": ["espada", "sable"], "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Juanito", "lastName": "Palotes", "gender": "masculino", "age": 29, "federateNumber": 2198, "club": "Sala de Armas de Valencia", "country": "España", "modality": ["espada", "sable", "florete"], "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "movil", "number": "66527627" } ]
},
{
  "firstName": "Rodrigo", "lastName": "Rodriguez", "gender": "masculino", "age": 64, "federateNumber": 21, "club": "Club de Esgrima Almassora", "country": "España", "modality": ["espada", "sable", "florete"], "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
}]
"""

const val JSON_TIRADORES_RAW = """
[{
  "firstName": "Juan", "lastName": "Ramirez", "gender": "masculino", "age": 24, "federateNumber": 2001, "club": "Sala de Armas del Ejercito", "country": "España", "modality": "espada", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Juan", "lastName": "Ramirez", "gender": "masculino", "age": 22, "federateNumber": 2034, "club": "Sala de Armas del Ejercito", "country": "España", "modality": "espada", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Maria", "lastName": "Ramirez", "gender": "masculino", "age": 18, "federateNumber": 2101, "club": "Club de Esgrima Valencia", "country": "España", "modality": "florete", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Juan", "lastName": "Rodriguez", "gender": "masculino", "age": 45, "federateNumber": 1015, "club": "Club de Esgrima Valencia", "country": "España", "modality": "espada", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Juan", "lastName": "Random", "gender": "masculino", "age": 42, "federateNumber": 1021, "club": "Club de Esgrima Valencia", "country": "España", "modality": "sable", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Pepa", "lastName": "Conill", "gender": "femenino", "age": 26, "federateNumber": 2901, "club": "Club de Esgrima Valencia", "country": "España", "modality": "florete", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "movil", "number": "66627627" } ]
},
{
  "firstName": "Francisco", "lastName": "Clausell", "gender": "masculino", "age": 34, "federateNumber": 3010, "club": "Real Club de Esgrima Madrid", "country": "España", "modality": "espada", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "movil", "number": "27627" } ]
},
{
  "firstName": "Pablo", "lastName": "Blanco", "gender": "masculino", "age": 28, "federateNumber": 2501, "club": "Club de Esgrima Valencia", "country": "España", "modality": "espada", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Paz", "lastName": "Gonzale", "gender": "femenino", "age": 21, "federateNumber": 2231, "club": "Club de Esgrima Tarragona", "country": "España", "modality": "sable", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
},
{
  "firstName": "Juanito", "lastName": "Palotes", "gender": "masculino", "age": 29, "federateNumber": 2198, "club": "Club de Esgrima Valencia", "country": "España", "modality": "espada", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "movil", "number": "66527627" } ]
},
{
  "firstName": "Rodrigo", "lastName": "Rodriguez", "gender": "masculino", "age": 64, "federateNumber": 21, "club": "Sala de Armas del Ejercito", "country": "España", "modality": "sable", "address": { "streetAddress": "Udhna", "city": "San Jone", "state": "CA", "postalCode": "95221" }, "phoneNumbers": [ { "type": "home", "number": "27627" } ]
}]
"""