INSERT INTO public.tipo_estado (id_tipo_estado,nombre) VALUES(1,'PROVEEDOR');
INSERT INTO public.tipo_estado (id_tipo_estado,nombre) VALUES(2,'ORDEN_PAGO');

INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(1, 'BORRADOR', 1);
INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(2, 'APROBADA', 1);
INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(3, 'RECHAZADA', 1);
INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(4, 'PAGADA', 1);
INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(5, 'ACTIVO', 2);
INSERT INTO public.estado (id_estado, nombre, id_tipo_estado) VALUES(6, 'INACTIVO', 2);

