function getFullXPath(element) {
    if (element === document.body) return '/html/body';

    let parts = [];
    while (element && element.nodeType === Node.ELEMENT_NODE) {
        let tagName = element.nodeName.toLowerCase();
        let parent = element.parentNode;

        if (parent) {
            let siblings = Array.from(parent.children).filter(e => e.nodeName === element.nodeName);
            if (siblings.length > 1) {
                let index = siblings.indexOf(element) + 1;
                tagName += `[${index}]`;
            }
        }

        parts.unshift(tagName);
        element = parent;
    }

    return '/' + parts.join('/');
}

function getRelativeXPath(element) {
    if (element.id) return `//*[@id="${element.id}"]`;
    let parts = [];
    while (element && element.nodeType === Node.ELEMENT_NODE) {
        let tag = element.nodeName.toLowerCase();
        if (element.id) {
            parts.unshift(`//*[@id="${element.id}"]`);
            break;
        } else {
            let sameTags = Array.from(element.parentNode.children).filter(e => e.nodeName === element.nodeName);
            let index = sameTags.indexOf(element) + 1;
            parts.unshift(tag + (sameTags.length > 1 ? `[${index}]` : ''));
        }
        element = element.parentNode;
    }
    let relativeXpath =  '//' + parts.join('/');
    return relativeXpath.replace('////', '//');
}