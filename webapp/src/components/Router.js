import React from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import App from "./App";       // App.js est dans components
import Sample from "./Sample";
import MapPage from "./MapPage";
import NotFound from "./NotFound";
import Navbar from "./Navbar";

export default function Router() {
  return (
    <BrowserRouter>
      <div>
        <Navbar />
        <Routes>
          <Route path="/" element={<App />} />
          <Route path="/sample" element={<Sample />} />
          <Route path="/MapPage" element={<MapPage />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </div>
    </BrowserRouter>
  );
}
