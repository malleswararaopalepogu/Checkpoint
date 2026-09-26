# Deployment

## Render backend

Create a Render Blueprint from the repository root and use `render.yaml`. The
backend is built from `Backend/checkpoint/Dockerfile`. Enter the existing
database and SMTP credentials when Render prompts for the variables marked
`sync: false`.

Set `DB_URL` to the JDBC form, for example
`jdbc:mysql://host:port/defaultdb?sslMode=REQUIRED`, not the `mysql://` URI.
Set `FRONTEND_URL` to the deployed Vercel origin, such as
`https://your-app.vercel.app` (no trailing slash).

## Vercel frontend

Create a Vercel project with `Frontend/checkpoint` as its root directory. Vercel
detects the Vite build automatically. Add this environment variable before
deploying:

| Name | Value |
| --- | --- |
| `VITE_BACKEND_URL` | `https://your-render-service.onrender.com/api/v1.0` |

The Vite variable is public in the browser bundle; do not put credentials or
private API keys in it. Redeploy the frontend after changing it. Set Render's
`FRONTEND_URL` to the final Vercel production domain so credentialed CORS
requests are accepted.