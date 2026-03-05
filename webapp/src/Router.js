// import React from 'react';
// import { BrowserRouter, Routes, Route } from "react-router-dom";
// import App from "./App";       // App.js est dans components
// import MapPage from "./MapPage";
// import NotFound from "./NotFound";
// import Navbar from "./Navbar";
//
// export default function Router() {
//   return (
//     <BrowserRouter>
//       <div>
//         <Navbar />
//         <Routes>
//           <Route path="/" element={<App />} />
//           <Route path="/MapPage" element={<MapPage />} />
//           <Route path="*" element={<NotFound />} />
//         </Routes>
//       </div>
//     </BrowserRouter>
//   );
// }

import React from 'react';
import { BrowserRouter, Routes, Route } from 'react-router-dom';
import Navbar from './Navbar';
import HomeView from './modules/home/views/HomeView';
import MapView from './modules/map/views/MapView';
import NotFound from './NotFound';

export default function Router() {
  return (
      <BrowserRouter>
        <Navbar />
        <Routes>
          <Route path="/" element={<HomeView />} />
          <Route path="/map" element={<MapView />} />
          <Route path="*" element={<NotFound />} />
        </Routes>
      </BrowserRouter>
  );
}
