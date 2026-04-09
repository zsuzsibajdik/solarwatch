import { Link } from 'react-router-dom';

export default function AuthLayout({ title, subtitle, children, footer }) {
  return (
    <div className="min-h-dvh bg-base-200 grid place-items-center p-4">
      <div className="w-full max-w-md">
        <div className="text-center mb-6">
          <Link to="/" className="inline-flex items-center gap-2 font-semibold text-xl">
            <span className="badge badge-primary badge-lg" />
            Solar Watch
          </Link>
        </div>

        <div className="card bg-base-100 shadow-xl">
          <div className="card-body">
            <h1 className="card-title text-2xl">{title}</h1>
            {subtitle ? <p className="text-base-content/70">{subtitle}</p> : null}
            <div className="mt-2">{children}</div>
          </div>
        </div>

        {footer ? <div className="text-center mt-4 text-sm text-base-content/70">{footer}</div> : null}
      </div>
    </div>
  );
}

