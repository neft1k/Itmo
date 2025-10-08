<template>
	<div class="kpis">
		<div class="kpi">
			<div class="title">Всего</div>
			<div class="value">{{ total ?? '—' }}</div>
			<div class="help">Количество объектов в выборке</div>
		</div>

		<div class="kpi">
			<div class="title">Средний расход</div>
			<div class="value">{{ avgFuel === null ? '—' : avgFuel.value }}</div>
			<div class="help">/ops/avg-fuel</div>
		</div>

		<div class="kpi">
		<div class="title">Транспорт с максимальным типом</div>
		<div class="value">
			<template v-if="maxTypeId">
			<div class="font-semibold">{{ maxTypeId.name }}</div>
			<div class="help">
				{{ maxTypeId.type }} — {{ maxTypeId.fuelType }}, {{ maxTypeId.enginePower }} л.с.
			</div>
			<div class="help">
				{{ maxTypeId.numberOfWheels }} колёс, вместимость {{ maxTypeId.capacity }} т
			</div>
			<div class="help">
				Пробег {{ maxTypeId.distanceTravelled }} км, расход {{ maxTypeId.fuelConsumption }} л/100км
			</div>
			<div class="help">
				Координаты: X={{ maxTypeId.coordinates?.x }}, Y={{ maxTypeId.coordinates?.y }}
			</div>
			</template>
			<template v-else>—</template>
		</div>
		</div>



		<div class="kpi">
			<div class="title">SSE статус</div>
			<div class="value">
				<span class="badge" :class="sseOnline ? 'ok' : 'off'">
					<span
						:style="{ width: '8px', height: '8px', borderRadius: '50%', display: 'inline-block', background: sseOnline ? '#30d158' : '#ff453a' }"></span>
					{{ sseOnline ? 'online' : 'offline' }}
				</span>
			</div>
			<div class="help">/stream/vehicles</div>
		</div>
	</div>
</template>


<script setup>
defineProps({
	total: Number,
	avgFuel: { type: [Number, String], default: null },
	maxTypeId: { type: [Number, String], default: null },
	sseOnline: Boolean
})
</script>
