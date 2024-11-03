const generateColor = (category) => {
    // Simple hash function to convert category name to a color
    const hash = Array.from(category).reduce((acc, char) => acc + char.charCodeAt(0), 0);
    const hue = hash % 360; // Get a hue value based on hash
    return `hsl(${hue}, 70%, 80%)`; // Use HSL for better color variety
  };
  