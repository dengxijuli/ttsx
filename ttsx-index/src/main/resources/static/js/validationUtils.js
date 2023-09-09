// validationUtils.js

export function isNotEmpty(value) {
    if (value === undefined) {
        return false;
    }
    if (typeof value !== 'string') {
        return false;
    }
    return value !== '' && value.trim() !== '';
}

export function isNumber(value) {
    if (value === undefined) {
        return false;
    }
    return !isNaN(parseFloat(value)) && isFinite(value);
}

export function isInteger(value) {
    if (value === undefined) {
        return false;
    }
    return Number.isInteger(Number(value));
}

export function isPositive(value) {
    if (value === undefined) {
        return false;
    }
    return Number(value) >= 0;
}

export function isBigDecimal(value) {
    if (value === undefined) {
        return false;
    }
    return typeof value === 'bigint' || typeof value === 'number';
}

export function isValidLength(value, maxLength) {
    if (value === undefined) {
        return false;
    }
    return String(value).length <= maxLength;
}