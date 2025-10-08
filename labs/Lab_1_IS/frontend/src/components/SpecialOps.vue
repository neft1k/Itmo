<template>
	<div class="panel">
		<h3 style="margin:0 0 8px">Спец-операции</h3>
		<div class="row">
			<div style="flex:1">
				<div class="label">name содержит…</div>
				<div class="row">
					<input class="input" v-model.trim="nameQ" placeholder="подстрока" />
					<button class="btn" @click="runNameContains">Выполнить</button>
				</div>
				<div v-if="nameResults.length" class="panel" style="margin-top:10px">
					<div class="label">Результаты поиска:</div>
					<table class="table">
						<thead>
						<tr>
							<th>ID</th>
							<th>Название</th>
							<th>Тип</th>
							<th>Колёс</th>
							<th>Мощность</th>
							<th>Топливо</th>
						</tr>
						</thead>
						<tbody>
						<tr v-for="v in nameResults" :key="v.id" class="row-card">
							<td>{{ v.id }}</td>
							<td>{{ v.name }}</td>
							<td>{{ v.type }}</td>
							<td>{{ v.numberOfWheels }}</td>
							<td>{{ v.enginePower }}</td>
							<td>{{ v.fuelType }}</td>
						</tr>
						</tbody>
					</table>
					</div>
					<div v-else class="help">Ничего не найдено.</div>
			</div>

			<div style="flex:1">
				<div class="label">число колёс: от/до</div>
				<div class="row">
					<input class="input" type="number" v-model.number="wFrom" placeholder="from" />
					<input class="input" type="number" v-model.number="wTo" placeholder="to" />
					<button class="btn" @click="runWheelsRange">Выполнить</button>
				</div>
				<div v-if="wheelResults.length" class="panel" style="margin-top:10px">
					<div class="label">Результаты поиска:</div>
					<table class="table">
						<thead>
							<tr>
								<th>ID</th>
								<th>Название</th>
								<th>Тип</th>
								<th>Колёс</th>
								<th>Мощность</th>
								<th>Топливо</th>
							</tr>
						</thead>
						<tbody>
							<tr v-for="v in wheelResults" :key="v.id" class="row-card">
								<td>{{ v.id }}</td>
								<td>{{ v.name }}</td>
								<td>{{ v.type }}</td>
								<td>{{ v.numberOfWheels }}</td>
								<td>{{ v.enginePower }}</td>
								<td>{{ v.fuelType }}</td>
							</tr>
						</tbody>
					</table>
				</div>

			</div>
		</div>
	</div>
</template>

<script setup>
import { ref } from 'vue'
import { OpsApi } from '../api/api'

const nameQ = ref('')
const nameResults = ref([])
const wFrom = ref()
const wTo = ref()
const wheelResults = ref([])

async function runNameContains() {
	try {
		const result = await OpsApi.nameContains(nameQ.value || '')
		nameResults.value = Array.isArray(result) ? result : []
	} catch (e) {
		toast(String(e.message || e))
	}
}

async function runWheelsRange() {
	try {
		const result = await OpsApi.wheelsRange(wFrom.value ?? '', wTo.value ?? '')
		wheelResults.value = Array.isArray(result) ? result : []
	} catch (e) {
		toast(String(e.message || e))
	}
}


function toast(msg) {
	const ev = new CustomEvent('toast', { detail: msg })
	window.dispatchEvent(ev)
}
</script>
