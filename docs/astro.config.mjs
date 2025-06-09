// @ts-check
import { defineConfig } from 'astro/config';
import starlight from '@astrojs/starlight';
import starlightThemeFlexoki from 'starlight-theme-flexoki'
import starlightSidebarTopics from 'starlight-sidebar-topics'

// https://astro.build/config
export default defineConfig({
	site: 'https://echeran.github.io/icu',
	base: '/icu',
	integrations: [
		starlight({
			title: 'ICU',
			social: [{ icon: 'github', label: 'GitHub', href: 'https://github.com/unicode-org/icu' }],
			plugins: [
				// Changes color scheme
				starlightThemeFlexoki({
					accentColor: "cyan",
				}),
				// Supercedes the default Starlight config for sidebar and instead
				// creates multiple disjoint sidebars.
				//
				// starlightSidebarTopics is pulled in via dependency from 
				// starlight-sidebar-topics-dropdown, which adds a dropdown that
				// toggles among the disjoint sidebars, showing only one at a
				// time, thereby creating the effect of distinct sub-sites within
				// the overall site.
				starlightSidebarTopics([
					{
						label: "About",
						icon: "information",
						link: "about/example",
						items: [
							{ label: 'Example', slug: 'about/example' },
							{ label: 'Other Page', slug: 'about/otherpage' },
						]
					},
					{
						label: "Using",
						icon: "laptop",
						link: "using/example",
						items: [
							{ label: 'Example', slug: 'using/example' },
							{ label: 'Other Page', slug: 'using/otherpage' },
						]
					},
					{
						label: "User Guide",
						icon: "open-book",
						link: "guide/example",
						items: [
							{ label: 'Example', slug: 'guide/example' },
							{ label: 'Other Page', slug: 'guide/otherpage' },
						]
					},
					{
						label: "Contribute",
						icon: "seti:todo",
						link: "dev/example",
						items: [
							{ label: 'Example', slug: 'dev/example' },
							{ label: 'Other Page', slug: 'dev/otherpage' },
						]
					},
				]),
			],
			components: {
				// Override the default `Sidebar` component with a custom one.
				Sidebar: './src/components/Sidebar.astro',
			},
		}),
	],
});
