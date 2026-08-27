import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import zhCn from 'element-plus/es/locale/lang/zh-cn'   // 中文
import 'highlight.js/styles/github-dark.css'            // 代码高亮样式（深色，配阅读页深色代码块）
import '@/styles/main.css'                              // InkHub 设计系统
import App from './App.vue'
import router from './router'

const app = createApp(App)
app.use(createPinia())
app.use(router)
app.use(ElementPlus, { locale: zhCn })
app.mount('#app')
