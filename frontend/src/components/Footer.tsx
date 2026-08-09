export function Footer() {
  const year = new Date().getFullYear()

  return (
    <footer className="border-t border-border bg-card">
      <div className="mx-auto w-full max-w-6xl px-8 py-8">
        <div className="flex flex-col gap-6 sm:flex-row sm:items-start sm:justify-between">
          <div>
            <p className="font-display text-lg font-semibold">Navora</p>
            <p className="mt-1 max-w-xs text-sm text-muted-foreground">
              A route through history, planned by you.
            </p>
            <div className="mt-3 flex items-center gap-3 font-mono-data text-xs text-muted-foreground">
              <span>29.9792° N, 31.1342° E</span>
              <span aria-hidden="true">···········</span>
              <span>26.9124° N, 75.7873° E</span>
            </div>
          </div>

          <div className="flex flex-col gap-1 text-sm text-muted-foreground sm:text-right">
            <span>Scoped to India and Egypt</span>
            <span>Built with LangGraph, Spring Boot, and PostGIS</span>
          </div>
        </div>

        <div className="mt-6 flex flex-col-reverse items-start justify-between gap-2 border-t border-border pt-4 text-xs text-muted-foreground sm:flex-row sm:items-center">
          <span>© {year} Navora. A personal project.</span>
          <div className="flex gap-4">
            <a href="#" className="hover:text-foreground">GitHub</a>
            <a href="#" className="hover:text-foreground">About</a>
          </div>
        </div>
      </div>
    </footer>
  )
}