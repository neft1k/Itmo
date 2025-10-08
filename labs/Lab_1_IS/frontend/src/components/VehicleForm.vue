<template>
	<form @submit.prevent="submit">
		<div class="grid" style="grid-template-columns: repeat(2, minmax(0,1fr));">
			<div>
				<div class="label">Name *</div>
				<input class="input" v-model.trim="model.name" placeholder="Roadster #1" />
				<div class="help" v-if="err.name">{{ err.name }}</div>
			</div>

			<div>
				<div class="label">Type *</div>
				<select class="input" v-model="model.type">
					<option v-for="t in types" :key="t" :value="t">{{ t }}</option>
				</select>
				<div class="help" v-if="err.type">{{ err.type }}</div>
			</div>

			<div>
				<div class="label">Engine Power *</div>
				<input class="input" type="number" v-model.number="model.enginePower" min="1" />
				<div class="help" v-if="err.enginePower">{{ err.enginePower }}</div>
			</div>

			<div>
				<div class="label">Number of Wheels</div>
				<input class="input" type="number" v-model.number="model.numberOfWheels" min="1"
					placeholder="пусто = null" />
				<div class="help" v-if="err.numberOfWheels">{{ err.numberOfWheels }}</div>
			</div>

			<div>
				<div class="label">Capacity *</div>
				<input class="input" type="number" step="0.01" v-model.number="model.capacity" min="0.01" />
				<div class="help" v-if="err.capacity">{{ err.capacity }}</div>
			</div>

			<div>
				<div class="label">Distance Travelled *</div>
				<input class="input" type="number" step="0.01" v-model.number="model.distanceTravelled" min="0.01" />
				<div class="help" v-if="err.distanceTravelled">{{ err.distanceTravelled }}</div>
			</div>

			<div>
				<div class="label">Fuel Consumption</div>
				<input class="input" type="number" v-model.number="model.fuelConsumption" min="1"
					placeholder="пусто = null" />
				<div class="help" v-if="err.fuelConsumption">{{ err.fuelConsumption }}</div>
			</div>

			<div>
				<div class="label">Fuel Type</div>
				<select class="input" v-model="model.fuelType">
					<option :value="null">—</option>
					<option v-for="f in fuels" :key="f" :value="f">{{ f }}</option>
				</select>
				<div class="help" v-if="err.fuelType">{{ err.fuelType }}</div>
			</div>

			<div>
				<div class="label">X *</div>
				<input class="input" type="number" v-model.number="model.coordinates.x" />
				<div class="help" v-if="err['coordinates.x']">{{ err['coordinates.x'] }}</div>
			</div>

			<div>
				<div class="label">Y * (≤ 751)</div>
				<input class="input" type="number" step="0.01" v-model.number="model.coordinates.y" :max="751" />
				<div class="help" v-if="err['coordinates.y']">{{ err['coordinates.y'] }}</div>
			</div>
		</div>

		<hr class="sep" />

		<div class="row" style="justify-content:flex-end">
			<button type="button" class="btn secondary" @click="$emit('cancel')">Отмена</button>
			<button type="submit" class="btn primary">{{ submitLabel }}</button>
		</div>
	</form>
</template>

<script setup>
import { reactive, watch, computed } from 'vue'
import { validateVehicle, VehicleType as types, FuelType as fuels, emptyVehicle } from '../utils/validation'

const props = defineProps({
	value: { type: Object, default: () => emptyVehicle() }
})
const emit = defineEmits(['submit', 'cancel'])

const model = reactive(JSON.parse(JSON.stringify(props.value)))
const err = reactive({})

watch(() => props.value, (v) => {
	Object.assign(model, JSON.parse(JSON.stringify(v || emptyVehicle())))
	Object.keys(err).forEach(k => delete err[k])
})

const submitLabel = computed(() => props.value?.id ? 'Сохранить' : 'Создать')

function submit() {
	Object.keys(err).forEach(k => delete err[k])
	const e = validateVehicle(model)
	Object.assign(err, e)
	if (Object.keys(e).length === 0) {
		const dto = JSON.parse(JSON.stringify(model))
		if (dto.numberOfWheels === '') dto.numberOfWheels = null
		if (dto.fuelConsumption === '') dto.fuelConsumption = null
		if (dto.fuelType === '') dto.fuelType = null
		emit('submit', dto)
	}
}
</script>
