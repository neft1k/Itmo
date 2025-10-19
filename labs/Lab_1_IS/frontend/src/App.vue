<template>
	<div class="container">
		<h1 style="margin:0 0 12px">Vehicle IS</h1>

		<div class="panel" style="margin-bottom:12px">
			<div class="row" style="gap:8px; align-items:center; flex-wrap:wrap">
				<span>Режим: <strong>{{ store.role === 'admin' ? 'Администратор' : 'Пользователь' }}</strong></span>
				<button class="btn secondary" @click="toggleRole">
					Сменить на {{ store.role === 'admin' ? 'пользователя' : 'админа' }}
				</button>
				<div class="row" style="gap:6px; align-items:center">
					<span class="label" style="margin:0">Имя:</span>
					<input class="input" v-model.trim="store.username" :disabled="store.role === 'admin'" style="width:160px" />
				</div>
			</div>
		</div>

		<KpiBar 
		:total="total" 
		:avgFuel="avgFuel" 
		:maxTypeId="maxTypeId" 
		:sseOnline="store.sseOnline" 
		/>

		<hr class="sep" />

		<VehicleTable ref="table" @create="openCreate" @edit="openEdit" @changed="refreshKpis"
			@update:total="(n)=> total=n" />

		<hr class="sep" />

		<SpecialOps />

		<hr class="sep" />

		<ImportPanel />

		<Modal v-if="showModal" @close="closeModal">
			<template #header>
				<h3 style="margin:0">{{ currentId ? `Изменить #${currentId}` : 'Создать Vehicle' }}</h3>
			</template>
			<VehicleForm :value="formValue" @submit="save" @cancel="closeModal" />
			<template #footer>
				<button class="btn secondary" @click="closeModal">Закрыть</button>
			</template>
		</Modal>

		<div class="toast" v-if="toastMsg">{{ toastMsg }}</div>
	</div>
</template>

<script setup>
import { ref, onMounted, onBeforeUnmount, inject } from 'vue'
import { VehiclesApi, OpsApi } from './api/api'
import { createVehicleStream } from './sse/sse'
import { emptyVehicle } from './utils/validation'

import Modal from './components/Modal.vue'
import VehicleForm from './components/VehicleForm.vue'
import VehicleTable from './components/VehicleTable.vue'
import KpiBar from './components/KpiBar.vue'
import SpecialOps from './components/SpecialOps.vue'
import ImportPanel from './components/ImportPanel.vue'

const store = inject('store')
const prevUserName = ref(store.username || 'user')

// KPI
const total = ref(0)
const avgFuel = ref(null)
const maxTypeId = ref(null)

async function refreshKpis() {
	try {
		avgFuel.value = await OpsApi.avgFuel()
	} catch { avgFuel.value = null }
	try {
		maxTypeId.value = await OpsApi.anyWithMaxType()
	} catch { maxTypeId.value = null }
}

const table = ref(null)


const toastMsg = ref('')
let toastTimer
function toast(msg) {
	toastMsg.value = msg
	clearTimeout(toastTimer)
	toastTimer = setTimeout(() => toastMsg.value = '', 3000)
}
window.addEventListener('toast', (e) => toast(e.detail))


const showModal = ref(false)
const currentId = ref(null)
const formValue = ref(emptyVehicle())

function openCreate() {
	currentId.value = null
	formValue.value = emptyVehicle()
	showModal.value = true
}

async function openEdit(id) {
	currentId.value = id
	try {
		formValue.value = await VehiclesApi.get(id)
		showModal.value = true
	} catch (e) { toast(String(e.message || e)) }
}

function closeModal() { showModal.value = false }

async function save(dto) {
	try {
		if (currentId.value) {
			await VehiclesApi.update(currentId.value, dto)
			toast(`Сохранено #${currentId.value}`)
		} else {
			const created = await VehiclesApi.create(dto)
			toast(`Создано #${created?.id ?? ''}`.trim())
		}
		showModal.value = false
		table.value?.$?.setupState?.reload?.()
		await refreshKpis()
	} catch (e) { toast(String(e.message || e)) }
}

function toggleRole() {
	if (store.role === 'admin') {
		store.role = 'user'
		store.username = prevUserName.value || 'user'
		toast('Режим: пользователь')
	} else {
		if (store.role === 'user' && store.username && store.username !== 'admin') {
			prevUserName.value = store.username
		}
		store.role = 'admin'
		store.username = 'admin'
		toast('Режим: администратор')
	}
}

// SSE
let stop
onMounted(() => {
	refreshKpis()
	stop = createVehicleStream({
		onOpen: () => { store.sseOnline = true },
		onError: () => { store.sseOnline = false },
		onInit: (data) => { store.lastEvent = { type: 'init', data } },
		onVehicle: (data) => { 
			store.lastEvent = { 
				type: 'vehicle', 
				data, 
				ts: Date.now() 
			} 
		}
	})
})
onBeforeUnmount(() => stop && stop())
</script>
