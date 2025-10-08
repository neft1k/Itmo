<template>
	<div class="panel">
		<!-- Фильтры -->
		<div class="row">
			<div style="flex:1">
				<div class="label">Фильтр: name (полное совпадение)</div>
				<input class="input" v-model.trim="filters.name" placeholder="Roadster #1" @keyup.enter="reload(0)" />
			</div>

			<div style="width:220px">
				<div class="label">type</div>
				<select class="input" v-model="filters.type" @change="reload(0)">
					<option value="">—</option>
					<option v-for="t in types" :key="t" :value="t">{{ t }}</option>
				</select>
			</div>
			<div style="width:220px">
				<div class="label">fuelType</div>
				<select class="input" v-model="filters.fuelType" @change="reload(0)">
					<option value="">—</option>
					<option v-for="f in fuels" :key="f" :value="f">{{ f }}</option>
				</select>
			</div>
			<div class="row" style="align-items:flex-end">
				<button class="btn" @click="reload(0)">Применить</button>
				<button class="btn secondary" @click="resetFilters">Сбросить</button>
				<button class="btn primary" @click="$emit('create')">+ Создать</button>
			</div>
		</div>

		<hr class="sep" />

		<!-- Таблица -->
		<div class="table-wrapper">
			<table class="table">
				<thead>
					<tr>
						<th @click="sortBy('id')">id
							<Sort :by="'id'" :state="sort" />
						</th>
						<th @click="sortBy('name')">name
							<Sort :by="'name'" :state="sort" />
						</th>
						<th>coordinates.x</th>
						<th>coordinates.y</th>
						<th>creationDate</th>
						<th>type</th>
						<th>enginePower</th>
						<th>numberOfWheels</th>
						<th>capacity</th>
						<th>distanceTravelled</th>
						<th>fuelConsumption</th>
						<th>fuelType</th>
						<th style="width:240px">Действия</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="v in content" :key="v.id" class="row-card">
						<td>{{ v.id }}</td>
						<td>{{ v.name }}</td>
						<td>{{ v.coordinates?.x }}</td>
						<td>{{ v.coordinates?.y }}</td>
						<td><span class="help">{{ v.creationDate }}</span></td>
						<td>{{ v.type }}</td>
						<td>{{ v.enginePower }}</td>
						<td>{{ v.numberOfWheels ?? '—' }}</td>
						<td>{{ v.capacity }}</td>
						<td>{{ v.distanceTravelled }}</td>
						<td>{{ v.fuelConsumption ?? '—' }}</td>
						<td>{{ v.fuelType ?? '—' }}</td>
						<td>
							<div class="row">
								<button class="btn" @click="$emit('edit', v.id)">Изменить</button>
								<button class="btn" @click="resetDistance(v.id)">Сбросить пробег</button>
								<button class="btn danger" @click="del(v.id)">Удалить</button>
							</div>
						</td>
					</tr>
					<tr v-if="!loading && content.length === 0">
						<td colspan="13" class="help">Нет данных</td>
					</tr>
				</tbody>
			</table>
		</div>

		<!-- Пагинация -->
		<div class="row" style="justify-content:space-between;align-items:center;margin-top:8px">
			<div class="help">Стр. {{ page + 1 }} / {{ pages }} · всего: {{ total }}</div>
			<div class="row">
				<button class="btn" :disabled="page <= 0" @click="reload(page - 1)">←</button>
				<input class="input" type="number" style="width:90px" :min="1" :max="pages"
					v-model.number.lazy="pageInput" @change="jumpTo" />
				<button class="btn" :disabled="page >= pages - 1" @click="reload(page + 1)">→</button>
				<select class="input" style="width:100px" v-model.number="size" @change="reload(0)">
					<option v-for="s in [5, 10, 20, 50]" :key="s" :value="s">{{ s }}/стр</option>
				</select>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref, reactive, watch, inject } from 'vue'
import { VehiclesApi, OpsApi } from '../api/api'
import { VehicleType as types, FuelType as fuels } from '../utils/validation'

const emit = defineEmits(['create', 'edit', 'changed'])

const store = inject('store')

const loading = ref(false)
const content = ref([])
const total = ref(0)

const page = ref(0)
const size = ref(10)
const pageInput = ref(1)

const sort = reactive({ field: 'id', dir: 'asc' })
const filters = reactive({ name: '', type: '', fuelType: '' })

function SortIcon({ field }) { return sort.field === field ? (sort.dir === 'asc' ? '↑' : '↓') : '' }

const Sort = {
	props: { by: String, state: Object },
	template: `<span class="help">{{ state.field===by ? (state.dir==='asc' ? '↑' : '↓') : '' }}</span>`
}

function sortBy(field) {
	if (sort.field === field) sort.dir = sort.dir === 'asc' ? 'desc' : 'asc'
	else { sort.field = field; sort.dir = 'asc' }
	reload(0)
}

function resetFilters() {
	filters.name = ''; filters.type = ''; filters.fuelType = '';
	reload(0)
}

async function reload(newPage = page.value) {
	loading.value = true
	try {
		const res = await VehiclesApi.list({
			page: newPage, 
			size: size.value,
			sort: sort.field, 
			dir: sort.dir,
			name: filters.name || undefined,
			type: filters.type || undefined,
			fuelType: filters.fuelType || undefined
		})
		content.value = res.content || res.items || res.data || []
		total.value = res.total ?? res.totalElements ?? 0
		page.value = res.page ?? res.number ?? newPage
		pageInput.value = page.value + 1
		emit('update:total', total.value)
	} catch (e) {
		toast(String(e.message || e))
	} finally {
		loading.value = false
	}
}

async function del(id) {
	if (!confirm(`Удалить #${id}? Связанные объекты также будут удалены.`)) return
	try {
		await VehiclesApi.delete(id)
		toast(`Удалено #${id}`)
		await reload(page.value)
		emit('changed')
	} catch (e) { toast(String(e.message || e)) }
}

async function resetDistance(id) {
	try {
		await OpsApi.resetDistance(id)
		toast(`Пробег сброшен у #${id}`)
		await reload(page.value)
		emit('changed')
	} catch (e) { toast(String(e.message || e)) }
}

function jumpTo() {
	const p = Math.max(1, Math.min(pageInput.value || 1, pages.value))
	reload(p - 1)
}

const pages = ref(1)
watch([total, size], () => {
	pages.value = Math.max(1, Math.ceil((total.value || 0) / (size.value || 10)))
})

function toast(msg) {
	const ev = new CustomEvent('toast', { detail: msg })
	window.dispatchEvent(ev)
}

watch(() => store.lastEvent, (s) => {
	if (!s) return
	reload(page.value)
})

reload(0)
</script>
