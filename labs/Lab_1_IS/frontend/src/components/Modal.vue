<template>
	<div class="modal-backdrop" @click.self="$emit('close')">
		<div class="modal">
			<div class="modal-header">
				<slot name="header" />
				<button class="btn secondary" @click="$emit('close')">✕</button>
			</div>
			<div class="modal-body">
				<slot />
			</div>
			<div class="modal-footer">
				<slot name="footer" />
			</div>
		</div>
	</div>
</template>
<script setup>
</script>
