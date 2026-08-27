import { marked } from 'marked'
import hljs from 'highlight.js'

// marked 配置：代码高亮
marked.setOptions({
  highlight(code, lang) {
    if (lang && hljs.getLanguage(lang)) {
      return hljs.highlight(code, { language: lang }).value
    }
    return hljs.highlightAuto(code).value
  },
  breaks: true,      // 换行 = <br>，写博客友好
  gfm: true,         // GitHub 风格 Markdown（表格等）
})

// 把 Markdown 原文转成 HTML 字符串（v-html 用）
export function renderMarkdown(md) {
  return md ? marked.parse(md) : ''
}
