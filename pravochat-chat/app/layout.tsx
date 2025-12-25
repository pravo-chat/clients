import type { Metadata } from "next";

export const metadata: Metadata = {
  title: "PravoChat — ИИ-юрист",
  description: "Онлайн консультации по российскому праву",
  alternates: {
    canonical: "https://pravochat.ru/",
  },
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="ru">
      <head>
        <meta name="viewport" content="width=device-width, initial-scale=1.0" />
        <link rel="icon" href="https://pravochat.ru/favicon.ico" />
      </head>
      <body style={{ margin: 0, padding: 0 }}>
        {children}
      </body>
    </html>
  );
}





