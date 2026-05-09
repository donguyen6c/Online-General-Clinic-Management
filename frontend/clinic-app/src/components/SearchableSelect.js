import { useState, useEffect, useRef } from "react";
import Apis from "../configs/Apis";

const SearchableSelect = ({ endpoint, onChange, placeholder = "Tìm kiếm...", labelKey = "name" }) => {
    const [query, setQuery] = useState("");
    const [options, setOptions] = useState([]);
    const [open, setOpen] = useState(false);
    const debounceRef = useRef(null);
    const wrapperRef = useRef(null);

    useEffect(() => {
        const handler = (e) => {
            if (!wrapperRef.current?.contains(e.target)) setOpen(false);
        };
        document.addEventListener("mousedown", handler);
        return () => document.removeEventListener("mousedown", handler);
    }, []);

    useEffect(() => {
        clearTimeout(debounceRef.current);
        if (!query.trim()) return setOptions([]);
        debounceRef.current = setTimeout(async () => {
            try {
                const res = await Apis.get(`${endpoint}?kw=${query}`);
                setOptions(res.data || []);
            } catch {
                setOptions([]);
            }
        }, 300);
    }, [query, endpoint]); 

    return (
        <div ref={wrapperRef} style={{ position: "relative" }}>
            <input
                className="form-control"
                placeholder={placeholder}
                value={query}
                onChange={(e) => { setQuery(e.target.value); setOpen(true); }}
            />
            {open && options.length > 0 && (
                <ul className="list-group shadow" style={{ position: "absolute", zIndex: 999, width: "100%" }}>
                    {options.map(item => (
                        <li key={item.id}
                            className="list-group-item list-group-item-action"
                            style={{ cursor: "pointer" }}
                            onMouseDown={() => { onChange(item); setQuery(item[labelKey]); setOpen(false); }}>
                            {item[labelKey]}  {item.id ? `(ID: ${item.id})` : ""}
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

export default SearchableSelect;