// useDebouncedEffect.js
import { useEffect } from 'react';

export const useDebouncedEffect = (callback, delay, dependencies) => {
    useEffect(() => {
        const handler = setTimeout(() => {
            callback();
        }, delay);

        // Cleanup: Xóa timeout khi dependencies thay đổi trước khi hết delay
        return () => clearTimeout(handler);

    }, [...dependencies, delay]);
};
