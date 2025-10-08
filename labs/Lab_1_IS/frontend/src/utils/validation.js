export const VehicleType = ['CAR','SHIP','HOVERBOARD']
export const FuelType = ['MANPOWER','PLASMA','ANTIMATTER']

export function validateVehicle(v) {
  const errors = {}

  if (!v.name || String(v.name).trim() === '') errors.name = 'Имя обязательно и не может быть пустым'

  if (!v.coordinates) errors.coordinates = 'Координаты обязательны'
  else {
    if (v.coordinates.x === null || v.coordinates.x === undefined) errors['coordinates.x'] = 'X обязателен'
    if (v.coordinates.y === null || v.coordinates.y === undefined) errors['coordinates.y'] = 'Y обязателен'
    else if (Number(v.coordinates.y) > 751) errors['coordinates.y'] = 'Y ≤ 751'
  }

  if (!v.type || !VehicleType.includes(v.type)) errors.type = 'Тип обязателен (CAR | SHIP | HOVERBOARD)'

  const gt0 = (n) => n !== null && n !== undefined && Number(n) > 0

  if (!gt0(v.enginePower)) errors.enginePower = 'Мощность > 0'
  if (v.numberOfWheels !== null && v.numberOfWheels !== undefined && !gt0(v.numberOfWheels))
    errors.numberOfWheels = 'Число колёс > 0 (или пусто)'
  if (!gt0(v.capacity)) errors.capacity = 'Вместимость > 0'
  if (!gt0(v.distanceTravelled)) errors.distanceTravelled = 'Пробег > 0'
  if (v.fuelConsumption !== null && v.fuelConsumption !== undefined && !gt0(v.fuelConsumption))
    errors.fuelConsumption = 'Расход > 0 (или пусто)'
  if (v.fuelType && !['MANPOWER','PLASMA','ANTIMATTER'].includes(v.fuelType))
    errors.fuelType = 'Недопустимое значение'

  return errors
}

export function emptyVehicle() {
  return {
    // id, creationDate — сервером
    name: '',
    coordinates: { x: null, y: null },
    type: 'CAR',
    enginePower: null,
    numberOfWheels: null,
    capacity: null,
    distanceTravelled: null,
    fuelConsumption: null,
    fuelType: null
  }
}
