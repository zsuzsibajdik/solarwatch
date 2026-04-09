export default function PageLoader({ label = 'Loading…' }) {
  return (
    <div className="min-h-[40vh] grid place-items-center">
      <div className="flex flex-col items-center gap-3">
        <span className="loading loading-spinner loading-lg text-primary" />
        <div className="text-sm text-base-content/70">{label}</div>
      </div>
    </div>
  );
}

