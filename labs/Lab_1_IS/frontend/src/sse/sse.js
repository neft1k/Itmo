import { SseApi } from '../api/api'

export function createVehicleStream({ onInit, onVehicle, onOpen, onError }) {
  const url = SseApi.streamUrl()
  const es = new EventSource(url)
  es.addEventListener('open', () => onOpen?.())
  es.addEventListener('error', (e) => onError?.(e))
  es.addEventListener('init', (e) => onInit?.(e.data))
  es.addEventListener('vehicle', (e) => onVehicle?.(e.data)) // "CREATED:ID", "UPDATED:ID", "DELETED:ID"
  return () => es.close()
}
