export default function AlertBanner({ type = 'error', title, children }) {
  const cls =
    type === 'success'
      ? 'alert alert-success'
      : type === 'warning'
        ? 'alert alert-warning'
        : type === 'info'
          ? 'alert alert-info'
          : 'alert alert-error';

  return (
    <div role="alert" className={cls}>
      <div>
        {title ? <h3 className="font-semibold">{title}</h3> : null}
        <div className="text-sm">{children}</div>
      </div>
    </div>
  );
}

