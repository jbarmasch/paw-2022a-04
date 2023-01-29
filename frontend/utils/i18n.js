import i18next from 'i18next';
import en from '../lang/en';
import es from '../lang/es';
import LngDetector from 'i18next-browser-languagedetector';

const detectionOptions = {
	order: ['path', 'cookie', 'navigator', 'localStorage', 'subdomain', 'queryString', 'htmlTag'],
	lookupFromPathIndex: 0,
};

i18next
  .use(LngDetector)
  .init({
    interpolation: {
      escapeValue: false,
    },
    detection: detectionOptions,
    fallbackLng: "en",
    resources: {
      es: {translation: es},
      en: {translation: en},
    },
  });

export default i18next;