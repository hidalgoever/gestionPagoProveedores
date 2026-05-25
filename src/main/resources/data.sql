INSERT INTO public.tipo_estado (id_tipo_estado,nombre) VALUES(1,'PROVEEDOR');
INSERT INTO public.tipo_estado (id_tipo_estado,nombre) VALUES(2,'ORDEN_PAGO');

INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(1, 'BORRADOR', 1);
INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(2, 'APROBADA', 1);
INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(3, 'RECHAZADA', 1);
INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(4, 'PAGADA', 1);
INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(5, 'ACTIVO', 2);
INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(6, 'INACTIVO', 2);

INSERT INTO public.proveedor (correo_electronico, identificacion_tributaria, nombre, id_estado) VALUES('hidalgaoever@live.com', '10697444', 'ana hidalgo', 5);
INSERT INTO public.proveedor (correo_electronico, identificacion_tributaria, nombre, id_estado) VALUES('hidalgaoever@live.com', '458258', 'pedro hidalgo', 5);
INSERT INTO public.proveedor (correo_electronico, identificacion_tributaria, nombre, id_estado) VALUES('hidalgaoever@live.com', '5866323', 'pejuandro gomez', 6);
INSERT INTO public.proveedor (correo_electronico, identificacion_tributaria, nombre, id_estado) VALUES('ever.hidalgo22@live.com', '10697999', 'ever orlando hidalgo', 6);

INSERT INTO public.orden_pago (concepto, fecha_creacion, monto, id_estado, id_proveedor) VALUES('Pago de servicios de ofina', '2026-05-24 12:10:46.662', 70.0, 1, 2);
INSERT INTO public.orden_pago (concepto, fecha_creacion, monto, id_estado, id_proveedor) VALUES('Pago de servicios de internet', '2026-05-24 12:12:20.304', 70.0, 1, 2);
INSERT INTO public.orden_pago (concepto, fecha_creacion, monto, id_estado, id_proveedor) VALUES('Pago de servicios de otros', '2026-05-24 12:12:30.924', 70.0, 1, 2);
INSERT INTO public.orden_pago (concepto, fecha_creacion, monto, id_estado, id_proveedor) VALUES('Pago de servicios de activos', '2026-05-24 12:12:37.922', 70.0, 1, 2);
INSERT INTO public.orden_pago (concepto, fecha_creacion, monto, id_estado, id_proveedor) VALUES('Pago de servicios de peajes', '2026-05-24 12:12:47.328', 70.0, 1, 2);
INSERT INTO public.orden_pago (concepto, fecha_creacion, monto, id_estado, id_proveedor) VALUES('Pago de servicios de vehiculos', '2026-05-24 12:12:56.758', 70.0, 1, 2);
INSERT INTO public.orden_pago (concepto, fecha_creacion, monto, id_estado, id_proveedor) VALUES('Pago de servicios de vuelos', '2026-05-24 12:18:18.607', 70.0, 1, 2);
INSERT INTO public.orden_pago (concepto, fecha_creacion, monto, id_estado, id_proveedor) VALUES('Pago de servicios de vuelos update', '2026-05-24 12:10:16.322', 70.0, 2, 2);

