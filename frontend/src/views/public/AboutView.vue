<script setup lang="ts">
import { onMounted, ref } from 'vue'
import SectionHeading from '@/components/SectionHeading.vue'
import { getProfile } from '@/api'

const profile = ref<Record<string, string>>({})
onMounted(async () => { profile.value = await getProfile() })
</script>

<template>
  <section class="container about-page">
    <SectionHeading eyebrow="A LITTLE CONTEXT" title="关于这个角落" description="我相信好的工程应该让人更专注于真正重要的问题。" />
    <div class="about-grid"><div class="about-large"><span class="about-number">01</span><h1>{{ profile.name || 'NovaBlog 作者' }}</h1><p>{{ profile.bio || '我是一名关注 Web 工程与产品体验的开发者。这个博客用来记录架构选择、代码实践，以及那些还没有标准答案的问题。' }}</p><p>我喜欢把模糊的想法拆成可以验证的小实验，也喜欢在项目结束后回头整理：哪里做得好，哪里还可以更简单。</p></div><div class="about-aside"><div><span class="eyebrow">FOCUS</span><p>Java · Vue · 产品工程</p></div><div><span class="eyebrow">LOCATION</span><p>{{ profile.location || 'China' }}</p></div><div><span class="eyebrow">LINKS</span><p><a :href="profile.github || 'https://github.com/'" target="_blank">GitHub ↗</a></p></div></div></div>
    <div class="principles"><span class="eyebrow">WORKING PRINCIPLES</span><div class="principle-row"><span>01</span><strong>先把问题说清楚</strong><p>清晰的边界会让方案自然变得简单。</p></div><div class="principle-row"><span>02</span><strong>用小步快跑验证</strong><p>可运行的反馈比完美的猜测更可靠。</p></div><div class="principle-row"><span>03</span><strong>留下可复用的答案</strong><p>写下来的经验，才有机会帮助下一个人。</p></div></div>
  </section>
</template>
