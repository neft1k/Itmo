<template>
	<div class="panel">
		<h3 style="margin:0 0 8px">Импорт объектов</h3>
	<div class="row" style="gap:8px; align-items:center; flex-wrap:wrap">
		<input type="file" accept="application/json,.json" ref="fileInput" @change="onFile" />
		<button class="btn" :disabled="!selectedFile || loading" @click="runImport">
			{{ loading ? 'Импорт...' : 'Импортировать' }}
		</button>
		<button class="btn secondary" :disabled="loadingHistory" @click="loadHistory">
			{{ loadingHistory ? 'Обновление...' : 'Обновить историю' }}
		</button>
		<span v-if="selectedFile" class="help">Выбрано: {{ selectedFile.name }}</span>
		<span class="help">Админ видит все операции, пользователь — только свои.</span>
	</div>

		<div v-if="history.length" style="margin-top:12px; overflow-x:auto">
			<table class="table">
				<thead>
					<tr>
						<th>ID</th>
						<th>Статус</th>
						<th>Файл</th>
						<th>Пользователь</th>
						<th>Роль</th>
						<th>Создано</th>
						<th>Начало</th>
						<th>Завершение</th>
						<th>Сообщение</th>
					</tr>
				</thead>
				<tbody>
					<tr v-for="job in history" :key="job.id">
						<td>{{ job.id }}</td>
						<td :class="['status', job.status]">{{ job.status }}</td>
						<td>{{ job.fileName }}</td>
						<td>{{ job.requestedBy }}</td>
						<td>{{ job.requestedRole }}</td>
						<td>{{ job.status === 'SUCCESS' ? (job.createdCount ?? 0) : '-' }}</td>
						<td>{{ formatDate(job.startedAt) }}</td>
						<td>{{ formatDate(job.finishedAt) }}</td>
						<td>{{ job.errorMessage || '—' }}</td>
					</tr>
				</tbody>
			</table>
		</div>
		<div v-else class="help" style="margin-top:10px">
			История пуста. Выполните импорт или обновите список.
		</div>
	</div>
</template>

<script setup>
import { ref, inject, onMounted, watch } from 'vue'
import { ImportApi } from '../api/api'

const store = inject('store')
const selectedFile = ref(null)
const loading = ref(false)
const loadingHistory = ref(false)
const history = ref([])
const fileInput = ref(null)

function onFile(e) {
	const files = e.target.files || []
	selectedFile.value = files[0] || null
}

async function runImport() {
	if (!selectedFile.value) {
		toast('Выберите JSON файл для импорта')
		return
	}
	loading.value = true
	try {
		await ImportApi.upload(selectedFile.value)
		toast('Импорт выполнен')
		selectedFile.value = null
		if (fileInput.value) fileInput.value.value = ''
		await loadHistory()
	} catch (e) {
		toast(String(e.message || e))
	} finally {
		loading.value = false
	}
}

async function loadHistory() {
	if (loadingHistory.value) return
	loadingHistory.value = true
	try {
		const list = await ImportApi.history()
		history.value = Array.isArray(list) ? list : []
	} catch (e) {
		history.value = []
		toast(String(e.message || e))
	} finally {
		loadingHistory.value = false
	}
}

function formatDate(val) {
	if (!val) return '—'
	try {
		const d = new Date(val)
		if (Number.isNaN(d.getTime())) return val
		return d.toLocaleString()
	} catch {
		return val
	}
}

function toast(msg) {
	const ev = new CustomEvent('toast', { detail: msg })
	window.dispatchEvent(ev)
}

onMounted(loadHistory)
watch(() => store.role, () => loadHistory())
</script>

<style scoped>
.status.SUCCESS { color: #2b8a3e; }
.status.FAILED { color: #d9480f; }
.status.IN_PROGRESS { color: #1c7ed6; }
</style>
