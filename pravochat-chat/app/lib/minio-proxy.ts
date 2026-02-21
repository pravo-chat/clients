const MINIO_ORIGIN = "http://155.212.170.94:9000/";
const PROXIED_PREFIX = "/minio/";

export function toProxiedContentUrl(str: string): string {
  if (typeof str !== "string") return str;
  return str.split(MINIO_ORIGIN).join(PROXIED_PREFIX);
}
