const BASE = (import.meta.env.VITE_API_BASE || 'http://localhost:8080/app/api').replace(/\/+$/, '')

function q(params = {}) {
	const url = new URL(BASE)
	return {
		url, search: (path, p = {}) => {
			const u = new URL(BASE + path)
			Object.entries(p).forEach(([k, v]) => {
				if (v !== undefined && v !== null && v !== '') u.searchParams.set(k, v)
			})
			return u.toString()
		}
	}
}

async function http(method, path, { params, body } = {}) {
	const url = params ? q().search(path, params) : (BASE + path)
	const res = await fetch(url, {
		method,
		headers: { 'Content-Type': 'application/json' },
		body: body ? JSON.stringify(body) : undefined
	})
	if (!res.ok) {
		let msg = await res.text().catch(() => '')
		try { const j = JSON.parse(msg); msg = j.message || msg } catch { }
		throw new Error(`${res.status} ${res.statusText}${msg ? ` – ${msg}` : ''}`)
	}
	const ct = res.headers.get('content-type') || ''
	return ct.includes('application/json') ? res.json() : res.text()
}

// Vehicles
export const VehiclesApi = {
	list: ({ page = 0, size = 10, sort, dir, name, type, fuelType } = {}) =>
		http('GET', '/vehicles', { params: { page, size, sort, dir, name, type, fuelType } }),
	get: (id) => http('GET', `/vehicles/${id}`),
	create: (dto) => http('POST', '/vehicles', { body: dto }),
	update: (id, dto) => http('PUT', `/vehicles/${id}`, { body: dto }),
	delete: (id) => http('DELETE', `/vehicles/${id}`),
}

// Ops
export const OpsApi = {
	avgFuel: () => http('GET', '/ops/avg-fuel'),
	anyWithMaxType: () => http('GET', '/ops/any-with-max-type'),
	nameContains: (qstr) => http('GET', '/ops/name-contains', { params: { q: qstr } }),
	wheelsRange: (from, to) => http('GET', '/ops/wheels-range', { params: { from, to } }),
	resetDistance: (id) => http('POST', `/ops/reset-distance/${id}`)
}

// SSE
export const SseApi = {
	streamUrl: () => BASE + '/stream/vehicles'
}
