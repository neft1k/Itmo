import { reactive } from 'vue'

export const store = reactive({
  sseOnline: false,
  lastEvent: null,
  username: 'user',
  role: 'user'
})
