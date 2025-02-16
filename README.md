Documentar depois sobre a paleta de cores no styles css.

Documentar sobre variaveis de ambientes no .env e no application.properties.

Breakpoint prefix	Minimum width	CSS
sm	40rem (640px)	@media (width >= 40rem) { ... }
md	48rem (768px)	@media (width >= 48rem) { ... }
lg	64rem (1024px)	@media (width >= 64rem) { ... }
xl	80rem (1280px)	@media (width >= 80rem) { ... }
2xl	96rem (1536px)	@media (width >= 96rem) { ... }

 /* colors */
  --color-primary: #0077B6; /* Deep Sky Blue */
  --color-secondary: #F4A261; /* Light Orange */
  --color-page: #F8F9FA; /* Light Grayish White */
  --color-component: #FFFFFF; /* White */
  --color-gray: #6C757D; /* Muted Gray */
  --color-gray-light: #CED4DA; /* Light Gray */
  --color-deep: #1E1E1E; /* Soft Black */
  --color-white-soft: #F9F9F9; /* Soft White */

futura funcionalidade Level:

public enum JobLevel {
    TRAINEE,   // Estagiário
    JUNIOR,    // Júnior
    MID,       // Pleno
    SENIOR,    // Sênior
    LEAD,      // Líder
    MANAGER;   // Gerente
}

E tentar armazenar a evolucao do funcionario.

feat: Commits, that adds or remove a new feature
fix: Commits, that fixes a bug
refactor: Commits, that rewrite/restructure your code, however does not change any API behaviour
perf: Commits are special refactor commits, that improve performance
style: Commits, that do not affect the meaning (white-space, formatting, missing semi-colons, etc)
test: Commits, that add missing tests or correcting existing tests
docs: Commits, that affect documentation only
build: Commits, that affect build components like build tool, ci pipeline, dependencies, project version, ...
ops: Commits, that affect operational components like infrastructure, deployment, backup, recovery, ...
chore: Miscellaneous commits e.g. modifying .gitignore
